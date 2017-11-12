package com.mastermind.services;

import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.impl.RepositoriesInMemoryImpl;
import com.mastermind.services.game.GameService;
import com.mastermind.services.game.responses.exceptions.NoActiveMatchException;
import com.mastermind.services.game.responses.exceptions.UserNotLoggedInException;
import com.mastermind.services.game.responses.types.AIPlayerRowData;
import com.mastermind.services.game.responses.types.UserGameState;
import com.mastermind.services.login.LoginService;
import com.mastermind.services.login.responses.LoginResponse;
import com.mastermind.services.players.PlayersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        service.newGame(0);
        UserGameState state = service.getUserGameState();
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
    void playSampleGame() {
        createLocalPlayerAndLogIn();
        createSampleEnemy();
        // Set number of rounds, or maintain defaults
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
                assertEquals(trialsBeforeCommit + 1, state.getTrials().size());
                assertEquals(UserGameState.MatchStatus.IN_PROGRESS, state.getMatchStatus());

            }
        }
        assertEquals(state.getMaxTrialCount(), state.getTrials().size());

    }

}
