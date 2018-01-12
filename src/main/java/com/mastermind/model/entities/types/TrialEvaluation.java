package com.mastermind.model.entities.types;

import com.mastermind.model.entities.base.Entity;

import java.util.Objects;

/**
 * Represents an evaluation of a Trial
 */
public class TrialEvaluation extends Entity {
    private int correctPlaceAndColorCount;
    private int correctColorCount;

    public TrialEvaluation(String s){
        String s5 = "[*]";
        String[] split = s.split(s5);
        correctPlaceAndColorCount = Integer.parseInt(split[0]);
        correctColorCount = Integer.parseInt(split[1]);
    }
    public TrialEvaluation(int correctPlaceAndColorCount, int correctColorCount) {
        this.correctPlaceAndColorCount = correctPlaceAndColorCount;
        this.correctColorCount = correctColorCount;
    }

    public int getCorrectPlaceAndColorCount() {
        return correctPlaceAndColorCount;
    }

    public int getCorrectColorCount() {
        return correctColorCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrialEvaluation that = (TrialEvaluation) o;
        return correctPlaceAndColorCount == that.correctPlaceAndColorCount &&
                correctColorCount == that.correctColorCount;
    }

    @Override
    public int hashCode() {

        return Objects.hash(correctPlaceAndColorCount, correctColorCount);
    }

    @Override
    public String toString() {
        return "E{" +
                "" + correctPlaceAndColorCount +
                ", " + correctColorCount +
                '}';
    }

    public String serialize() {
        String s5 = "*";
        return correctPlaceAndColorCount + s5 + correctColorCount;
    }
}
