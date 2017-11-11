package com.mastermind.model.entities.types;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RoundTest {
    Round round;
    MatchConfig matchConfig;

    @BeforeEach
    void setUp() {
        Match match = mock(Match.class);
        matchConfig = new MatchConfig();
        when(match.getConfig()).thenReturn(matchConfig);
        round = new Round(match);
    }

    @Test
    void hasNextTrial() {
        assertTrue(round.hasNextTrial());
        setAllElements(1);
        round.commitMove();
        IntStream.range(0, matchConfig.getMaxTrialCount())
                .forEach(i -> {
                    setAllElements();
                    round.commitMove();
                });
        assertFalse(round.hasNextTrial());
    }

    @Test
    void commitMove() {
        // Set and commit the code
        setAllElements(1);
        // The current trial should be null
        assertNull(round.getCurrentTrial());
        round.commitMove();
        // The current trial should still be null
        assertNull(round.getCurrentTrial());
        // Set and commit first trial
        setAllElements();
        // No trials should be committed yet
        assertNull(round.getLastCommittedTrial());
        round.commitMove();
        Trial t1 = round.getCurrentTrial();
        assertNotNull(t1);
        setAllElements();
        // Current trial should be the next one
        assertNotSame(round.getCurrentTrial(), t1);
    }


    @Test
    void isFinished() {

    }

    @Test
    void getCurrentTrial() {
        assertNull(round.getCurrentTrial());
        // Set the code
        setAllElements(1);
        // Current trial should still be null after setting code elements
        assertNull(round.getCurrentTrial());
        round.commitMove();
        // And even after commiting the code
        assertNull(round.getCurrentTrial());
        // Create the first trial
        setAllElements();
        // It should now return the first trial
        Trial t1 = round.getCurrentTrial();
        assertNotNull(t1);
        // And should continue to be the same trial after setting elements
        setAllElements();
        Trial t2 = round.getCurrentTrial();
        assertSame(t1, t2);
        round.commitMove();
        // And still after committing
        Trial t3 = round.getCurrentTrial();
        assertSame(t1, t3);
        // But no longer after starting a new trial
        setAllElements();
        assertNotSame(t1, round.getCurrentTrial());
        round.commitMove();
    }

    @Test
    void getLastCommittedTrial() {
        // Set the code
        assertNull(round.getLastCommittedTrial());
        setAllElements();
        round.commitMove();
        // For the moment only the code was set
        assertNull(round.getLastCommittedTrial());
        // Set and commit first trial
        setAllElements();
        // She first trial is still uncommitted...
        assertNull(round.getLastCommittedTrial());
        Trial t1 = round.getCurrentTrial();
        assertNotNull(t1);
        round.commitMove();
        // Now it should return the sme first trial t1
        Trial t2 = round.getLastCommittedTrial();
        assertNotNull(t2);
        assertSame(t1, t2);
        // It should still be the same trial since no new one has started
        assertSame(t1, round.getCurrentTrial());
        setAllElements();
        // It should now be the next one
        assertNotSame(t1, round.getCurrentTrial());
    }

    @Test
    void isCurrentTrialCommitted() {
        assertFalse(round.isCurrentTrialCommitted());
        setAllElements(1);
        assertFalse(round.isCurrentTrialCommitted());
        round.commitMove();
        assertTrue(round.isCurrentTrialCommitted());
        setAllElements();
        assertFalse(round.isCurrentTrialCommitted());
        round.commitMove();
        assertTrue(round.isCurrentTrialCommitted());
        setAllElements();
        assertFalse(round.isCurrentTrialCommitted());
    }

    @Test
    void setElement() {
    }

    private void setAllElements() {
        setAllElements(0);
    }

    private void setAllElements(int e) {
        IntStream.range(0, matchConfig.getSlotCount())
                .forEach(i -> round.setElement(i, e));
    }
}