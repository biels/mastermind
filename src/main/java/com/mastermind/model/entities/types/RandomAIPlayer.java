package com.mastermind.model.entities.types;

public class RandomAIPlayer extends AIPlayer {
    private Long seed;

    public Long getSeed() {
        return seed;
    }

    public void setSeed(Long seed) {
        this.seed = seed;
    }
}
