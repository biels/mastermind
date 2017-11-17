package com.mastermind.model.entities.types;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {
    private Match match;
    private MatchConfig config;

    @BeforeEach
    void setUp() {
        config = new MatchConfig();
        config.setLocalStartsMakingCode(false);
        FillAIPlayer enemyPlayer = new FillAIPlayer();
        HumanPlayer localPlayer = new HumanPlayer();
        match = new Match(localPlayer, enemyPlayer, config);
    }

    @Test
    void isModified() {
        assertFalse(match.isModified());
        match.setElement(0, 0);
        assertTrue(match.isModified());
    }

    @Test
    void getCurrentRoundStartCM() {
        config.setLocalStartsMakingCode(true);
        FillAIPlayer enemyPlayer = new FillAIPlayer(0, 0);
        HumanPlayer localPlayer = new HumanPlayer();
        match = new Match(localPlayer, enemyPlayer, config);
        Round r1 = match.getCurrentRound();
        assertNotNull(r1);
        //First round should be as CM
        assertTrue(r1.isActivePlayerCodemaker());
        setAllElements(2);
        match.commitMove();
        // Winner should be human
        assertTrue(match.getLastFinishedRoundWinner().isPresent());
        assertSame(localPlayer, match.getLastFinishedRoundWinner().get());
        //Now human should take codebreaker turn
        assertTrue(match.isCodebreakerTurn());
        while (match.isCodebreakerTurn()){
            // 1 to ensure it does not match the code
            setAllElements(1);
            match.commitMove();
            assertNotSame(r1, match.getCurrentRound());
        }
        // The winner of the round should be enemy
        assertTrue(match.getLastFinishedRoundWinner().isPresent());
        assertSame(enemyPlayer, match.getLastFinishedRoundWinner().get());
        // At the end of the round, the currentRound should be finished
        assertTrue(r1.isFinished());
        Round r2 = match.getCurrentRound();
        // r2 should be a new round
        assertNotSame(r2, r1);
        // And it should be waiting for the code to be set
        assertTrue(r2.isActivePlayerCodemaker());
    }
    @Test
    void getCurrentRoundStartCB() {
        config.setLocalStartsMakingCode(false);
        FillAIPlayer enemyPlayer = new FillAIPlayer(0, 0);
        HumanPlayer localPlayer = new HumanPlayer();
        match = new Match(localPlayer, enemyPlayer, config);
        Round r1 = match.getCurrentRound();
        assertNotNull(r1);
        //First round should be as CB
        assertFalse(r1.isActivePlayerCodemaker());
        //Now human should take codebreaker turn
        assertTrue(match.isCodebreakerTurn());
        while (match.isCodebreakerTurn()){
            // 1 to ensure it does not match the code
            setAllElements(0);
            match.commitMove();
            assertNotSame(r1, match.getCurrentRound());
        }
        // The winner of the round should be human
        assertTrue(match.getLastFinishedRoundWinner().isPresent());
        assertSame(localPlayer, match.getLastFinishedRoundWinner().get());
        // At the end of the round, the currentRound should be finished
        assertTrue(r1.isFinished());
        Round r2 = match.getCurrentRound();
        // r2 should be a new round
        assertNotSame(r2, r1);
        // And it should be waiting for the code to be set
        assertTrue(r2.isActivePlayerCodemaker());
    }

    @Test
    void getLastFinishedRound() {
        assertNull(match.getLastFinishedRound());
        // Set an element to begin round
        match.setElement(0, 1);
        assertNotNull(match.getCurrentRound());
        assertNull(match.getLastFinishedRound());
        // Finish the round losing
        Round currentRound = match.getCurrentRound();
        while (currentRound.hasNextTrial()){
            setAllElements(1);
            match.commitMove();
        }
        Round r1 = match.getLastFinishedRound();
        assertNotNull(r1);
        assertTrue(r1.getWinner().isPresent());
        assertEquals(match.getEnemyPlayer(), r1.getWinner().get());
        match.setElement(0, 1);
        // Current round should be a new one
        assertNotSame(match.getCurrentRound(), r1);

    }

    @Test
    void setElement() {
        assertNotNull(match.getCurrentRound());
        //Play all rounds
        for (int i = 0; i < match.getConfig().getRoundCount(); i++) {
            // Play round
            Round round = match.getCurrentRound();
            while (!round.isFinished()){
                // 2 to ensure it does not match
                setAllElements(2);
                match.commitMove();
            }
        }
        assertThrows(RuntimeException.class, () -> match.setElement(0, 0));
    }
    @Test
    void playAllVsAI(){
        playVsAI(false, 3, 4);
        playVsAI(true, 4, 4);
        playVsAI(true, 7, 10);
        playVsAI(false, 3, 1);
        playVsAI(false, 1, 4);
        playVsAI(false, 1, 1);
    }

    private void playVsAI(boolean localStartsMakingCode, int roundCount, int maxTrialCount){
        config = new MatchConfig();
        config.setLocalStartsMakingCode(localStartsMakingCode);
        config.setMaxTrialCount(maxTrialCount);
        config.setRoundCount(roundCount);
        FillAIPlayer enemyPlayer = new FillAIPlayer();
        HumanPlayer localPlayer = new HumanPlayer();
        match = new Match(localPlayer, enemyPlayer, config);
        int playCountCm = 0;
        int playCountCb = 0;
        double localEloBefore = localPlayer.getElo();
        while (!match.isFinished()){
            // 3 to ensure it does not match
            int e = 4;//playCountCm % 3;
            setAllElements(e);
            if (match.getCurrentRound().isActivePlayerCodemaker()) playCountCm++;
            else playCountCb++;
            match.commitMove();
        }
        double localEloAfter = localPlayer.getElo();
        if(roundCount % 2 == 0)
        assertEquals(playCountCm * maxTrialCount, playCountCb);
        if(match.getWinner().equals(localPlayer)){
            assertTrue(localEloBefore < localEloAfter);
        }else{
            assertTrue(localEloBefore > localEloAfter);
        }
    }

    private void setAllElements() {
        setAllElements(0);
    }
    private void setAllElements(int e) {
        IntStream.range(0, match.getConfig().getSlotCount())
                .forEach(i -> match.setElement(i, e));
    }


}
