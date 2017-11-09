package com.mastermind.logic;

import com.mastermind.model.entities.types.Match;

public abstract class AlgorithmComponent {
    public abstract void playAsCodemaker(Match match);

    public abstract void playAsCodebreaker(Match match);
}
