package com.mastermind.services.game.responses.types;

public class EvaluationData {
    private int correctPlaceAndColorCount;
    private int correctColorCount;

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
