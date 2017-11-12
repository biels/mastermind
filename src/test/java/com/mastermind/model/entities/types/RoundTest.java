package com.mastermind.model.entities.types;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RoundTest {
    Round round;
    MatchConfig matchConfig;
    private Match matchMock;

    @BeforeEach
    void setUp() {
        matchMock = mock(Match.class);
        matchConfig = new MatchConfig();
        when(matchMock.getConfig()).thenReturn(matchConfig);
    }

    @Test
    void hasNextTrialCM() {
        round = new Round(matchMock, new HumanPlayer("human"), new FillAIPlayer());
        assertTrue(round.hasNextTrial());
        setAllElements(2);
        round.commitMove();
        assertFalse(round.hasNextTrial());
    }
    @Test
    void hasNextTrialCB() {
        round = new Round(matchMock, new FillAIPlayer(), new HumanPlayer("human"));
        assertTrue(round.hasNextTrial());
        IntStream.range(0, matchConfig.getMaxTrialCount())
                .forEach(i -> {
                    setAllElements(3);
                    round.commitMove();
                });
        assertFalse(round.hasNextTrial());
    }

    @Test
    void commitMoveHvsH() {
        round = new Round(matchMock, new HumanPlayer("human"), new HumanPlayer("human"));
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
    void isFinishedAIvsAI() {
        round = new Round(matchMock, new FillAIPlayer(), new FillAIPlayer());
        assertTrue(round.isFinished());
    }

    @Test
    void getCurrentTrialHvsH() {
        round = new Round(matchMock, new HumanPlayer("human"), new HumanPlayer("human"));
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
    void getCurrentTrialCM() {
        round = new Round(matchMock, new HumanPlayer("human"), new FillAIPlayer());
        assertNull(round.getCurrentTrial());
        // Set the code
        setAllElements(1);
        // Current trial should still be null after setting code elements
        assertNull(round.getCurrentTrial());
        round.commitMove();
        assertTrue(round.isFinished());
        assertNotNull(round.getLastCommittedTrial());
        assertSame(round.getLastCommittedTrial(), round.getCurrentTrial());
    }
    @Test
    void getCurrentTrialCB() {
        round = new Round(matchMock, new FillAIPlayer(), new HumanPlayer("human"));
        // The current trial should still be null
        assertNull(round.getCurrentTrial());
        // Create the first trial
        setAllElements();
        // It should now return the first trial
        Trial t1 = round.getCurrentTrial();
        assertNotNull(t1);
        // And should continue to be the same trial after setting elements
        setAllElements(2);
        Trial t2 = round.getCurrentTrial();
        assertSame(t1, t2);
        round.commitMove();
        // And still after committing
        Trial t3 = round.getCurrentTrial();
        assertSame(t1, t3);
        // But no longer after starting a new trial
        setAllElements(2);
        assertNotSame(t1, round.getCurrentTrial());
        round.commitMove();
    }

    @Test
    void getLastCommittedTrialHvsH() {
        round = new Round(matchMock, new HumanPlayer("human1"), new HumanPlayer("human2"));
        // Set the code
        assertNull(round.getLastCommittedTrial());
        setAllElements(0);
        round.commitMove();
        // For the moment only the code was set
        assertNull(round.getLastCommittedTrial());
        // Set and commit first trial
        setAllElements(1);
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
        setAllElements(0);
        // It should now be the next one
        assertNotSame(t1, round.getCurrentTrial());
        round.commitMove();
        // Codebreaker should have won now
        assertTrue(round.isFinished());
        assertTrue(round.getWinner().isPresent());
        assertEquals("human2", round.getWinner().get().getName());
    }

    @Test
    void isCurrentTrialCommittedHvsH() {
        round = new Round(matchMock, new HumanPlayer("human"), new HumanPlayer("human"));
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