package com.mastermind.model.entities.types;

import com.mastermind.logic.ComponentManager;

public class RandomAIPlayer extends AIPlayer {
    private Long seed;


    public RandomAIPlayer() {
        this("random-ai", 0x1L);
    }

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
    public void playAsCodemaker(Round match) {
        ComponentManager.getRandomAlgorithmComponent().playAsCodemaker(match);
    }

    @Override
    public void playAsCodebreaker(Round match) {
        ComponentManager.getRandomAlgorithmComponent().playAsCodebreaker(match);
    }

    @Override
    protected void deserializeSpecific(String specific) {
        seed = Long.parseLong(specific);
    }

    @Override
    protected String serializeSpecific() {
        return Long.toString(seed);
    }
}
