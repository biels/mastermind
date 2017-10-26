package com.mastermind.model.entities.types;

import com.mastermind.model.entities.base.Entity;

import java.util.ArrayList;

/**
 * Represents a round in a Match.
 */
public class Round extends Entity {
    private Player codemaker;
    private Player codebreaker;
    private Combination code;
    private ArrayList<Trial> trials;

    public Player getCodemaker() {
        return codemaker;
    }

    public void setCodemaker(Player codemaker) {
        this.codemaker = codemaker;
    }

    public Player getCodebreaker() {
        return codebreaker;
    }

    public void setCodebreaker(Player codebreaker) {
        this.codebreaker = codebreaker;
    }

    public Combination getCode() {
        return code;
    }

    public void setCode(Combination code) {
        this.code = code;
    }

    public ArrayList<Trial> getTrials() {
        return trials;
    }

    public void setTrials(ArrayList<Trial> trials) {
        this.trials = trials;
    }
}
