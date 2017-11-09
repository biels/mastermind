package com.mastermind.model.entities.types;

public abstract class AIPlayer extends Player {

    AIPlayer(String name) {
        super(name);
    }

    public abstract void playAsCodemaker(Match match);

    public abstract void playAsCodebreaker(Match match);
}
