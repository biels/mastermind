package com.mastermind.model.entities.types;

import com.mastermind.logic.ComponentManager;
import com.mastermind.logic.EvaluatorComponent;
import com.mastermind.model.entities.base.Entity;

/**
 * Represents a Trial in a Round
 */
public class Trial extends Entity {
    private Round round;

    private Combination combination;
    private TrialEvaluation trialEvaluation;

    public Trial(Round round, String s) {
        this.round = round;
        String s4 = "[+]";
        String[] split = s.split(s4);
        combination = new Combination(split[0]);
        trialEvaluation = new TrialEvaluation(split[1]);
    }

    public Trial(Round round) {
        this.round = round;
        combination = new Combination(round.getMatch().getConfig().getSlotCount());
    }

    public void evaluate() {
        EvaluatorComponent evaluator = ComponentManager.getEvaluatorComponent();
        MatchConfig config = round.getMatch().getConfig();
        trialEvaluation = evaluator.evaluate(round.getCode(), combination, config.getColorCount());
    }

    public Combination getCombination() {
        return combination;
    }

    public TrialEvaluation getTrialEvaluation() {
        return trialEvaluation;
    }

    public boolean isEvaluated() {
        return trialEvaluation != null;
    }

    @Override
    public String toString() {
        return "T{" + combination + ", " + trialEvaluation + "}";
    }

    public String serialize() {
        String s4 = "+";

        return combination.serialize() + s4 + trialEvaluation.serialize();
    }

}
