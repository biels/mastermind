package com.mastermind.model.entities.types;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {
    private Match match;

    @BeforeEach
    void setUp() {
        Player local = new HumanPlayer("Human", "1234");
        Player enemy = new RandomAIPlayer("RandomAI", 0L);
        MatchConfig config = new MatchConfig();
        config.setSlotCount(4);
        config.setColorCount(4);
        config.setMaxTrialCount(3);
        config.setAllowRepetition(true);
        match = new Match(config);
        match.setLocalPlayer(local);
        match.setEnemyPlayer(enemy);
    }

    @Test
    void initialState() {
        assertFalse(match.isInitialized());
        assertNull(match.getCurrentRound());
        assertNull(match.getLastFinishedRound());
        assertTrue(match.hasNextRound());
        assertNotNull(match.getLocalPlayer());
    }

    @Test
    void playSampleGame() {
        int codebreakerPlayCount = 0;
        int codemakerPlayCount = 0;
        while (!match.isFinished()) {
            //match.beginTurn();
            if (match.isCodemakerTurn()) {
                // Playing round as codemaker
                match.setElement(0, 1);
                match.setElement(1, 1);
                match.setElement(2, 2);
                match.setElement(3, 2);
                match.commitTrial();
                assertFalse(match.isCodemakerTurn());
                codemakerPlayCount++;
            }
            if (match.isCodebrekerTurn()) {
                // Playing round as codebreaker
                //assertFalse(match.isActivePlayerCodemaker());
                while (match.isCodebrekerTurn()) {
                    match.setElement(0, 0);
                    match.setElement(1, 0);
                    match.setElement(2, 1);
                    match.setElement(3, 1);
                    match.commitTrial();
                }
                assertFalse(false);
                codebreakerPlayCount++;
            }
        }
        int roundCount = match.getConfig().getRoundCount();
        assertEquals(roundCount, match.getRounds().size());
        assertEquals(roundCount / 2, codebreakerPlayCount);
        assertEquals(roundCount / 2, codemakerPlayCount);
    }

}
