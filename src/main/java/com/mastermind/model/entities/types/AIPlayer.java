package com.mastermind.model.entities.types;

public abstract class AIPlayer extends Player {

    AIPlayer(String name) {
        super(name);
    }

    public abstract void playAsCodemaker(Round match);

    public abstract void playAsCodebreaker(Round match);
}
