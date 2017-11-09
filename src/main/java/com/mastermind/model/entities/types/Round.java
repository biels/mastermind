package com.mastermind.model.entities.types;

import com.mastermind.model.entities.base.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a round in a Match.
 */
public class Round extends Entity {
    private Match match;

    private Player codemaker;
    private Player codebreaker;
    private Player winner;
    private Combination code;
    private int committedTrialIndex = -1;
    private boolean isActivePlayerCodemaker = true;
    private List<Trial> trials = new ArrayList<>();

    Round(Match match) {
        this.match = match;
        code = new Combination(getMatch().getConfig().getSlotCount());
    }

    public boolean hasNextTrial() {
        return trials.size() < match.getConfig().getMaxTrialCount();
    }

    public void newTrial() {
        if (!hasNextTrial()) throw new RuntimeException("Round has already reached the maximum number of trials.");
        Trial newTrial = new Trial(this);
        trials.add(newTrial);
    }

    void commitTrial() {
        if (isActivePlayerCodemaker()) {
            isActivePlayerCodemaker = false;
            return;
        }
        getCurrentTrial().evaluate();
        committedTrialIndex++;
        if (getLastCommittedTrial() != null && getLastCommittedTrial().getTrialEvaluation().getCorrectPlaceAndColorCount() == match.getConfig().getSlotCount()) {
            winner = codebreaker;
            return;
        }
        if (!hasNextTrial()) {
            winner = codemaker;
        }
    }

    public boolean isFinished() {
        return winner != null;
    }

    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }

    private int getTrialIndex() {
        return trials.size() - 1;
    }

    private boolean isInitialized() {
        return getTrialIndex() >= 0;
    }

    public Trial getCurrentTrial() {
        if (!isInitialized()) return null;
        return trials.get(getTrialIndex());
    }

    public Trial getLastCommittedTrial() {
        if (committedTrialIndex < 1) return null;
        return trials.get(committedTrialIndex);
    }

    public boolean isActivePlayerCodemaker() {
        return isActivePlayerCodemaker;
    }

    public Match getMatch() {
        return match;
    }

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

    public List<Trial> getTrials() {
        return trials;
    }

    public void setTrials(List<Trial> trials) {
        this.trials = trials;
    }

    // Current trial delegates
    public boolean isCurrentTrialCommitted() {
        return committedTrialIndex == getTrialIndex();
    }

    private Combination getFocusedCombination() {
        return isActivePlayerCodemaker ? getCode() : getCurrentTrial().getCombination();
    }

    public Integer setElement(int index, Integer element) {
        Combination combination = getFocusedCombination();
        return combination.setElement(index, element);
    }
}
