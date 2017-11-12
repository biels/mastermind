package com.mastermind.logic;

import com.mastermind.model.entities.types.Match;
import com.mastermind.model.entities.types.Round;

import java.util.stream.IntStream;

public abstract class AlgorithmComponent {
    public abstract void playAsCodemaker(Round match);

    public abstract void playAsCodebreaker(Round match);

}
