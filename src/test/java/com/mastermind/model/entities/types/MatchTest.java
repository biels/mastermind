package com.mastermind.model.entities.types;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {
    private Match match;

    @BeforeEach
    void setUp() {
        HumanPlayer local = new HumanPlayer("Human", "1234");
        FillAIPlayer enemy = new FillAIPlayer();
        MatchConfig config = new MatchConfig();
        config.setLocalStartsMakingCode(false);
        match = new Match(local, enemy, config);
    }

    @Test
    void isInitialized() {
        assertFalse(match.isInitialized());
        match.setElement(0, 0);
        assertTrue(match.isInitialized());
    }

    @Test
    void getCurrentRound() {
        Round r = match.getCurrentRound();
        assertNull(r);
        // Should be starting as codebreaker
        while (!match.isCurrentRoundFinished()){
            // 1 to ensure it does not match the code
            setAllElements(1);
            match.commitMove();
            assertNotSame(r, match.getCurrentRound());
        }
        // The winner of the round should not be null
        assertNotNull(match.getCurrentRound().getWinner());
        // At the end of the round, the currentRound should be finished
        assertTrue(match.isCurrentRoundFinished());
        Round r2 = match.getCurrentRound();
        // Set some elements to begin a new round
        setAllElements();
        assertNotSame(r2, r);
    }

    @Test
    void getLastFinishedRound() {
        assertNull(match.getLastFinishedRound());
        // Set an element to begin round
        match.setElement(0, 1);
        assertNotNull(match.getCurrentRound());
        assertNull(match.getLastFinishedRound());
        // Finish the round losing
        while (!match.isCurrentRoundFinished()){
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

    private void setAllElements() {
        setAllElements(0);
    }
    private void setAllElements(int e) {
        IntStream.range(0, match.getConfig().getSlotCount())
                .forEach(i -> match.setElement(i, e));
    }

    @Nested
    class Gameplay {
        @BeforeEach
        void setUp() {

            MatchConfig config = new MatchConfig();
            config.setSlotCount(4);
            config.setColorCount(4);
            config.setMaxTrialCount(3);
            config.setAllowRepetition(true);
            match = new Match(new HumanPlayer("human1", "1234"), new FillAIPlayer());
        }

        @Test
        void initialState() {
            assertFalse(match.isInitialized());
            assertNull(match.getCurrentRound());
            assertNull(match.getLastFinishedRound());
            assertNotNull(match.getLocalPlayer());
        }

        @Test
        void playMatch1() {
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
                    match.commitMove();
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
                        match.commitMove();
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

}
