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

    Round(Match match, Player codemaker, Player codebreaker) {
        this.match = match;
        this.codemaker = codemaker;
        this.codebreaker = codebreaker;
        code = new Combination(getMatch().getConfig().getSlotCount());
        // Play AI as codemaker if needed
        playAITurnCM();
    }

    public boolean hasNextTrial() {
        if(isFinished()) return false;
        return trials.size() < match.getConfig().getMaxTrialCount();
    }

    private void newTrial() {
        if (!hasNextTrial()) throw new RuntimeException("Round has already reached the maximum number of trials.");
        if (isFinished()) throw new RuntimeException("Round has already finished.");
        Trial newTrial = new Trial(this);
        trials.add(newTrial);
    }

    /**
     * Commits the code if playing as a codemaker or the current trial if playing as codebreaker
     */
    public void commitMove() {
        if (isActivePlayerCodemaker()) {
            // It is code
            isActivePlayerCodemaker = false;
            playAITurnCB();
            return;
        }
        // It is a trial
        getCurrentTrial().evaluate();
        // Actually commit
        committedTrialIndex++;
        // Check winner
        checkWinner();
    }

    private void checkWinner() {
        if (getLastCommittedTrial() != null && getLastCommittedTrial().getTrialEvaluation().getCorrectPlaceAndColorCount() == match.getConfig().getSlotCount()) {
            winner = codebreaker;
            return;
        }
        if (!hasNextTrial()) {
            winner = codemaker;
        }
    }

    private void playAITurnCB() {
        Player codebreaker = getCodebreaker();
        if (codebreaker instanceof AIPlayer) {
            ((AIPlayer) codebreaker).playAsCodebreaker(this);
        }
    }

    private void playAITurnCM() {
        Player codemaker = getCodemaker();
        if (codemaker instanceof AIPlayer) {
            ((AIPlayer) codemaker).playAsCodemaker(this);
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
        if (committedTrialIndex < 0) return null;
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
        if(isActivePlayerCodemaker) return false;
        return committedTrialIndex == getTrialIndex();
    }

    private Combination getFocusedCombination() {
        return isActivePlayerCodemaker ? getCode() : getCurrentTrial().getCombination();
    }

    public Integer setElement(int index, Integer element) {
        if(isCurrentTrialCommitted()) newTrial();
        Combination combination = getFocusedCombination();
        return combination.setElement(index, element);
    }
}
