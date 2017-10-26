package com.mastermind.model.entities.types;

import com.mastermind.model.entities.base.Entity;

/**
 * Represents a Trial in a Round
 */
public class Trial extends Entity {
    private Round round;

    private Combination combination;
    private TrialEvaluation trialEvaluation;



    public Combination getCombination() {
        return combination;
    }

    public void setCombination(Combination combination) {
        this.combination = combination;
    }

    public TrialEvaluation getTrialEvaluation() {
        return trialEvaluation;
    }

    public void setTrialEvaluation(TrialEvaluation trialEvaluation) {
        this.trialEvaluation = trialEvaluation;
    }
}
