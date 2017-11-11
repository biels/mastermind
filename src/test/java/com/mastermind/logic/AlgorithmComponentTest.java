package com.mastermind.logic;

import com.mastermind.model.entities.types.Match;
import com.mastermind.model.entities.types.MatchConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

abstract class AlgorithmComponentTest<T extends AlgorithmComponent> {
    protected T component;
    Match match;

    abstract T getInstance();

    @BeforeEach
    void setUp() {
        component = getInstance();
        assertNotNull(component);
        match = mock(Match.class);
        // Config
        when(match.isCurrentRoundFinished()).thenReturn(false, false, false, true);
        when(match.getConfig()).thenReturn(new MatchConfig());
    }

    @Test
    void playsAsCodemaker() {
        component.playAsCodemaker(match);
        IntStream.range(0, match.getConfig().getSlotCount())
                .forEach(i -> verify(match).setElement(eq(i), anyInt()));
        verify(match).commitMove();
    }

    @Test
    void playsAsCodebreaker() {
        component.playAsCodebreaker(match);

        IntStream.range(0, match.getConfig().getSlotCount())
                .forEach(j -> verify(match).setElement(eq(j), anyInt()));
        verify(match).commitMove();

    }
}