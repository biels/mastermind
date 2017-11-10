package com.mastermind.model.entities.types;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrialTest {
    Round round;
    private Trial trial;
    private MatchConfig matchConfig;

    @BeforeEach
    void setUp() {
        round = mock(Round.class);
        matchConfig = new MatchConfig();
        when(round.getMatch()).thenReturn(new Match(matchConfig));
        trial = new Trial(round);
    }

    @Test
    void evaluate() {
        IntStream.range(0, matchConfig.getSlotCount())
                .forEach(i -> trial.getCombination().setElement(i, 0));
        when(round.getCode()).thenReturn(trial.getCombination());
        assertNull(trial.getTrialEvaluation());
        trial.evaluate();
        assertNotNull(trial.getTrialEvaluation());
    }

    @Test
    void getCombination() {
        assertNotNull(trial.getCombination());
        assertEquals(matchConfig.getSlotCount(), trial.getCombination().getSize());
    }
}