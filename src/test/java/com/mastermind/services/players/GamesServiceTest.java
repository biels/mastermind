package com.mastermind.services.players;

import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.impl.RepositoriesInMemoryImpl;
import com.mastermind.services.ServiceManager;
import com.mastermind.services.game.GameService;
import com.mastermind.services.game.responses.types.AIPlayerRowData;
import com.mastermind.services.game.responses.types.UserGameState;
import com.mastermind.services.login.LoginService;
import com.mastermind.services.login.responses.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GamesServiceTest {
    private GameService service;
    private List<AIPlayerRowData> enemyPlayers;
    private PlayersService playersService;
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        RepositoryManager.attachImplementation(new RepositoriesInMemoryImpl());
        ServiceManager.initState();
        service = ServiceManager.getGameService();
        playersService = ServiceManager.getPlayersService();
        loginService = ServiceManager.getLoginService();
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

    @Nested
    class Gameplay {

        //TODO: Split test into basic functions and make many test cases
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
                    state = check(service.commitTrial());
                    //  assertEquals(roundBeforeCommit + 1, state.getCurrentRound());
                } else {
                    // Break combination

                    // Same trial for all rounds
                    service.placeColor(0, 0);
                    service.placeColor(1, 1);
                    service.placeColor(2, 3);
                    service.placeColor(3, 2);
                    state = service.getUserGameState();
                    int trialsBeforeCommit = state.getTrials().size();
                    state = check(service.commitTrial());
//                    assertEquals(trialsBeforeCommit + 1, state.getTrials().size());
//                    assertEquals(UserGameState.MatchStatus.IN_PROGRESS, state.getMatchStatus());

                }
            }
            assertEquals(state.getMaxTrialCount(), state.getTrials().size());

        }
    }

    @Test
    void newGame() {
        createLocalPlayerAndLogIn();
        createSampleEnemy();
        UserGameState state = service.newGame(0);
        check(state);
    }

    @Nested
    class StateMapping {
        @Test
        void initialState() {
            UserGameState state = check(service.getUserGameState());
            assertEquals(UserGameState.MatchStatus.NOT_STARTED, state.getMatchStatus());
        }
//        @Test
//        void mapping() {
//            // Sample data
//            HumanPlayer localPlayer = new HumanPlayer();
//            localPlayer.setName("local");
//            localPlayer.setPassword("12345");
//            ServiceManager.getState().setLoggedInPlayer(localPlayer);
//
//            RandomAIPlayer enemyPlayer = new RandomAIPlayer();
//            enemyPlayer.setName("enemy");
//            enemyPlayer.setSeed(0x233FL);
//
//            Match match = new Match();
//            assertEquals(new MatchConfig(), match.getConfig());
//            match.getConfig().setRoundCount(2);
//            match.getConfig().setColorCount(5);
//            match.getConfig().setSlotCount(4);
//            match.getConfig().setAllowRepetition(true);
//            match.setLocalPlayer(localPlayer);
//            match.setEnemyPlayer(enemyPlayer);
//            match.setFinished(false);
//            match.setRounds(IntStream.range(0, match.getConfig().getRoundCount())
//                    .mapToObj(i -> {
//                        Round round = new Round(match);
//                        Combination code = new Combination();
//                        code.setElements(IntStream.range(0,  match.getConfig().getSlotCount())
//                                .map(i2 -> (i2 + i) % match.getConfig().getColorCount())
//                                .boxed().collect(Collectors.toList()));
//                        round.setCode(code);
//                        if(i % 2 == 0){
//                            round.setCodebreaker(localPlayer);
//                            round.setCodemaker(enemyPlayer);
//                        }else {
//                            round.setCodebreaker(enemyPlayer);
//                            round.setCodebreaker(localPlayer);
//                        }
//                        round.setTrials(IntStream.range(0, match.getConfig().getMaxTrialCount())
//                        .boxed().map(i2 -> {
//                                    Trial trial = new Trial();
//                                    Combination combination = new Combination();
//                                    combination.setElements(IntStream.range(0,  match.getConfig().getSlotCount())
//                                            .map(i3 -> (i3 + i + 1) % match.getConfig().getColorCount())
//                                            .boxed().collect(Collectors.toList()));
//                                    trial.setCombination(combination);
//                                    TrialEvaluation trialEvaluation = new TrialEvaluation();
//                                    trialEvaluation.setCorrectColorCount(i2 % match.getConfig().getSlotCount() / 2);
//                                    trialEvaluation.setCorrectPlaceAndColorCount(i2 % match.getConfig().getSlotCount() / 2);
//                                    trial.setTrialEvaluation(trialEvaluation);
//                                    return trial;
//                                }).collect(Collectors.toList()));
//                        return round;
//                    })
//                    .collect(Collectors.toList())
//            );
//            ServiceManager.getState().setActiveMatch(match);
//
//            UserGameState state = check(service.getUserGameState());
//
//            // Config
//            assertEquals(match.getConfig().getRoundCount(), state.getTotalRoundCount());
//            assertEquals(match.getConfig().getColorCount(), state.getColorCount());
//            assertEquals(match.getConfig().getMaxTrialCount(), state.getMaxTrialCount());
//            assertEquals(match.getConfig().getSlotCount(), state.getSlotCount());
//            assertEquals(match.getConfig().isAllowRepetition(), state.isAllowRepetition());
//
//
//
//        }
    }

}
