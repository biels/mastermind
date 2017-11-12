package com.mastermind.model.entities.types;

import com.mastermind.logic.ComponentManager;

public class MinimaxAIPlayer extends AIPlayer {
    private int depth;

    public MinimaxAIPlayer(String name, int depth) {
        super(name);
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public void playAsCodemaker(Round match) {
        ComponentManager.getMinimaxAlgorithmComponent().playAsCodemaker(match);
    }

    @Override
    public void playAsCodebreaker(Round match) {
        ComponentManager.getMinimaxAlgorithmComponent().playAsCodebreaker(match);
    }
}
