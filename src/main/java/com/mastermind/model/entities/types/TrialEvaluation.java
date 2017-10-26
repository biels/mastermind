package com.mastermind.model.entities.types;

import com.mastermind.model.entities.base.Entity;

/**
 * Represents an evaluation of a Trial
 */
public class TrialEvaluation extends Entity {
    private Trial trial;
    private int correctPlaceAndColorCount;
    private int correctColorCount;

    public Trial getTrial() {
        return trial;
    }

    public void setTrial(Trial trial) {
        this.trial = trial;
    }

    public int getCorrectPlaceAndColorCount() {
        return correctPlaceAndColorCount;
    }

    public void setCorrectPlaceAndColorCount(int correctPlaceAndColorCount) {
        this.correctPlaceAndColorCount = correctPlaceAndColorCount;
    }

    public int getCorrectColorCount() {
        return correctColorCount;
    }

    public void setCorrectColorCount(int correctColorCount) {
        this.correctColorCount = correctColorCount;
    }
}
