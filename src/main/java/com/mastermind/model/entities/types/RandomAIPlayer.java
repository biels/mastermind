package com.mastermind.model.entities.types;

import com.mastermind.logic.ComponentManager;

public class RandomAIPlayer extends AIPlayer {
    private Long seed;

    public RandomAIPlayer(String name, Long seed) {
        super(name);
        this.seed = seed;
    }

    public Long getSeed() {
        return seed;
    }

    public void setSeed(Long seed) {
        this.seed = seed;
    }

    @Override
    public void playAsCodemaker(Match match) {
        ComponentManager.getRandomAlgorithmComponent().playAsCodemaker(match);
    }

    @Override
    public void playAsCodebreaker(Match match) {
        ComponentManager.getRandomAlgorithmComponent().playAsCodebreaker(match);
    }
}
