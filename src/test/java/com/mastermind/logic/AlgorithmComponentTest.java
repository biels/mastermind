package com.mastermind.logic;

import com.mastermind.model.entities.types.Match;
import com.mastermind.model.entities.types.MatchConfig;
import com.mastermind.model.entities.types.Round;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

abstract class AlgorithmComponentTest<T extends AlgorithmComponent> {
    protected T component;
    Round round;
    private MatchConfig config;

    abstract T getInstance();

    @BeforeEach
    void setUp() {
        component = getInstance();
        assertNotNull(component);
        round = mock(Round.class);
        // Config
        when(round.isFinished()).thenReturn(false, false, false, true);
        Match matchMock = mock(Match.class);
        config = new MatchConfig();
        when(matchMock.getConfig()).thenReturn(config);
        when(round.getMatch()).thenReturn(matchMock);
    }
    @Test
    void playsAsCodemaker() {
        component.playAsCodemaker(round);
        IntStream.range(0, config.getSlotCount())
                .forEach(i -> verify(round).setElement(eq(i), anyInt()));
        verify(round).commitMove();
    }

    @Test
    void playsAsCodebreaker() {
        // Simulate it ends at 3rd trial
        when(round.hasNextTrial()).thenReturn(true, true, true, false);
        component.playAsCodebreaker(round);

        IntStream.range(0, config.getSlotCount())
                .forEach(j -> {
                    verify(round, atLeast(4)).hasNextTrial();
                    verify(round, atLeast(3)).setElement(eq(j), anyInt());
                });
        //It should commit exactly 3 trials
        verify(round, times(3)).commitMove();

    }
}