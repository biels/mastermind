package com.mastermind.services;

import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.impl.RepositoriesInMemoryImpl;
import com.mastermind.services.game.GameService;
import com.mastermind.services.game.responses.exceptions.NoActiveMatchException;
import com.mastermind.services.game.responses.exceptions.UserNotLoggedInException;
import com.mastermind.services.game.responses.types.AIPlayerRowData;
import com.mastermind.services.game.responses.types.SavedGameRowData;
import com.mastermind.services.game.responses.types.UserGameState;
import com.mastermind.services.login.LoginService;
import com.mastermind.services.login.responses.LoginResponse;
import com.mastermind.services.players.PlayersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    private GameService service;
    private List<AIPlayerRowData> enemyPlayers;
    private PlayersService playersService;
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        RepositoryManager.attachImplementation(new RepositoriesInMemoryImpl());
        ServiceManager.restart();
        service = ServiceManager.getGameService();
        playersService = ServiceManager.getPlayersService();
        loginService = ServiceManager.getLoginService();
    }

    @Test
    void listEnemyPlayers() {
        assertNotNull(service.listEnemyPlayers());
        assertEquals(0, service.listEnemyPlayers().size());
        createSampleEnemy();
        assertEquals(1, service.listEnemyPlayers().size());

    }

    private UserGameState check(UserGameState state) {
        assertNotNull(state);
        if (state.getMatchStatus() == UserGameState.MatchStatus.NOT_STARTED) return state;
        assertNotNull(state.getLocalPlayerName());
        assertNotNull(state.getEnemyPlayerName());
        assertNotNull(state.getLocalPlayerRole());
        assertNotEquals(0, state.getSlotCount());
        assertNotEquals(0, state.getColorCount());
        if (state.getLocalPlayerRole() == UserGameState.Role.CODEMAKER) {
            assertNotNull(state.getCode());
            assertNotNull(state.getCode().getElements());
            assertEquals(state.getSlotCount(), state.getCode().getElements().size());
        }
        assertNotNull(state.getTrials());
        assertTrue(state.getMaxTrialCount() >= state.getTrials().size());
        assertTrue(state.getTotalRoundCount() >= state.getCurrentRound());
        assertNotEquals(0, state.getTotalRoundCount());


        return state;
    }

    private void createSampleEnemy() {
        playersService.createRandomAIPlayer("random-ai", 0x88FDL);
        enemyPlayers = service.listEnemyPlayers();
        assertEquals(1, enemyPlayers.size());
        assertEquals("random-ai", enemyPlayers.get(0).getName());
    }

    private void createLocalPlayerAndLogIn() {
        playersService.createHumanPlayer("human", "1234");
        LoginResponse human = loginService.login("human", "1234");
        assertTrue(human.isSuccess());
        assertNotNull(ServiceManager.getState().getLoggedInPlayer());
    }

    @Test
    void newGame() {
        createLocalPlayerAndLogIn();
        createSampleEnemy();
        UserGameState state = service.newGame(0);
        assertEquals(ServiceManager.getState().getLoggedInPlayer().getName(), state.getLocalPlayerName());
        check(state);
    }

    @Test
    void repeatGame() {
        createLocalPlayerAndLogIn();
        createSampleEnemy();
        service.newGame(0);
        // Place one color to create game
        service.placeColor(0, 0);
        assertEquals(UserGameState.MatchStatus.IN_PROGRESS, service.getUserGameState().getMatchStatus());
        service.repeatGame();
        assertEquals(UserGameState.MatchStatus.NOT_STARTED, service.getUserGameState().getMatchStatus());


    }

    @Test
    void placeColor() {
        assertThrows(UserNotLoggedInException.class, () -> service.placeColor(0, 1));
        createLocalPlayerAndLogIn();
        assertThrows(NoActiveMatchException.class, () -> service.placeColor(0, 1));
        createSampleEnemy();
        UserGameState state = service.getUserGameState();
        assertEquals(UserGameState.MatchStatus.NOT_CREATED, state.getMatchStatus());
        service.newGame(0);
        state = service.getUserGameState();
        assertEquals(UserGameState.MatchStatus.NOT_STARTED, state.getMatchStatus());
        check(state);
        service.placeColor(0, 1);
        state = service.getUserGameState();
        check(state);
        assertEquals(UserGameState.MatchStatus.IN_PROGRESS, state.getMatchStatus());
        if (state.getLocalPlayerRole() == UserGameState.Role.CODEMAKER) {
            assertNotNull(state.getCode().getElements());
            assertEquals(1, (int) state.getCode().getElements().get(0));
        }
        if (state.getLocalPlayerRole() == UserGameState.Role.CODEBREAKER) {
            assertNotNull(state.getTrials().get(0).getCombinationData().getElements());
            assertEquals(1, (int) state.getTrials().get(0).getCombinationData().getElements().get(0));
        }

    }

    @Test
    void duplicatePreviousTrial() {
        createLocalPlayerAndLogIn();
        createSampleEnemy();
        service.setLocalStartsMakingCode(true);
        service.newGame(0);
        IntStream.range(0, service.getUserGameState().getSlotCount())
                .forEach(i -> service.placeColor(i, 1));
        service.commitMove();
        IntStream.range(0, service.getUserGameState().getSlotCount())
                .forEach(i -> service.placeColor(i, 2));
        service.commitMove();
        service.duplicatePreviousTrial();
        assertEquals(2, service.getUserGameState().getTrials().size());
        assertEquals(2, service.getUserGameState().getTrials().get(1).getCombinationData().getElements().get(0).intValue());
    }

    @Test
    void playSampleGame() {
        createLocalPlayerAndLogIn();
        createSampleEnemy();
        // Set number of rounds, or maintain defaults
        service.setLocalStartsMakingCode(true);
        UserGameState state = service.newGame(0);
        check(state);
        assertEquals(UserGameState.MatchStatus.NOT_STARTED, state.getMatchStatus());
        while (state.getMatchStatus() != UserGameState.MatchStatus.FINISHED) {
            // Round
            UserGameState.Role role = state.getLocalPlayerRole();
            if (role == UserGameState.Role.CODEMAKER) {
                // Set combination
                service.placeColor(0, 0);
                service.placeColor(1, 1);
                service.placeColor(2, 1);
                service.placeColor(3, 2);
                int roundBeforeCommit = state.getCurrentRound();
                state = check(service.commitMove());
                assertEquals(roundBeforeCommit + 1, state.getCurrentRound());
            } else {
                // Break combination

                // Same trial for all rounds
                service.placeColor(0, 0);
                service.placeColor(1, 1);
                service.placeColor(2, 3);
                service.placeColor(3, 2);
                state = service.getUserGameState();
                int trialsBeforeCommit = state.getTrials().size();
                state = check(service.commitMove());
                if (trialsBeforeCommit < state.getMaxTrialCount())
                    assertEquals(trialsBeforeCommit, state.getTrials().size());
                if (trialsBeforeCommit < state.getMaxTrialCount())
                    assertEquals(UserGameState.MatchStatus.IN_PROGRESS, state.getMatchStatus());

            }
        }
        assertEquals(state.getMaxTrialCount(), state.getTrials().size());
        assertEquals(UserGameState.MatchStatus.FINISHED, state.getMatchStatus());

    }

    @Test
    void playGameVsMinimaxWinning() {
        createLocalPlayerAndLogIn();
        playersService.createMinmiaxAIPlayer("minimax-ai", 6);
        service.setMaxTrialCount(1);
        service.setRoundCount(1);
        service.setSlotCount(5);
        service.setColorCount(8);
        service.setLocalStartsMakingCode(true);
        service.newGame(0);
        for (int i = 0; i < 5; i++) {
            service.placeColor(i, 0);
        }
        UserGameState state = service.commitMove();
        assertEquals(UserGameState.MatchStatus.FINISHED, state.getMatchStatus());
        assertTrue(state.getLocalWins());
    }

    @Test
    void listSavedGames() {
        createLocalPlayerAndLogIn();
        createSampleEnemy();
        service.newGame(0);
        List<SavedGameRowData> savedGameRows = service.listSavedGames().getSavedGameRows();
        assertNotNull(savedGameRows);
        assertEquals(0, savedGameRows.size());
        IntStream.range(0, service.getUserGameState().getSlotCount())
                .forEach(i -> service.placeColor(i, 0));
        assertEquals(1, service.listSavedGames().getSavedGameRows().size());
        service.newGame(0);
        service.placeColor(0, 0);
        assertEquals(2, service.listSavedGames().getSavedGameRows().size());
    }

    @Test
    void removeSavedGame() {
        createLocalPlayerAndLogIn();
        createSampleEnemy();
        service.setLocalStartsMakingCode(true);
        service.newGame(0);
        service.placeColor(0, 0);
        service.newGame(0);
        service.placeColor(0, 1);
        service.newGame(0);
        service.placeColor(0, 2);
        assertEquals(3, service.listSavedGames().getSavedGameRows().size());
        service.removeSavedGame(1);
        assertEquals(2, service.listSavedGames().getSavedGameRows().size());
        service.loadSavedGame(1);
        assertEquals(2, service.getUserGameState().getCode().getElements().get(0).intValue());
    }

    @Test
    void loadSavedGame() {
        createLocalPlayerAndLogIn();
        createSampleEnemy();
        String playerName = "random-ai-2";
        playersService.createRandomAIPlayer(playerName, 0x88AAAL);
        service.setLocalStartsMakingCode(true);
        service.newGame(1);
        service.placeColor(0, 1);
        service.newGame(0);
        service.placeColor(0, 0);
        service.loadSavedGame(0);
        UserGameState state = check(service.getUserGameState());
        assertEquals(playerName, state.getEnemyPlayerName());
        assertEquals(1, service.getUserGameState().getCode().getElements().get(0).intValue());


    }

    @Test
    void configEditing() {
        createLocalPlayerAndLogIn();
        createSampleEnemy();
        service.setColorCount(3);
        assertEquals(3, service.getUserGameState().getColorCount());
        service.setAllowRepetition(false);
        assertFalse(service.getUserGameState().isAllowRepetition());
        service.setLocalStartsMakingCode(false);
        assertFalse(service.getUserGameState().isLocalStartsMakingCode());
        service.newGame(0);
        // Expect 4 because 3 is lower than the number of slots, which would be i
        assertEquals(4, service.getUserGameState().getColorCount());
        assertFalse(service.getUserGameState().isAllowRepetition());
        assertFalse(service.getUserGameState().isLocalStartsMakingCode());
        service.setColorCount(4);
        service.setMaxTrialCount(5);
        assertEquals(4, service.getUserGameState().getColorCount());
        assertEquals(5, service.getUserGameState().getMaxTrialCount());
        // Create a game
    }
}
