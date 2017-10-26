package com.mastermind.services.game.responses.types;

public class TrialData {
    private CombinationData combinationData;
    private EvaluationData evaluationData;

    public CombinationData getCombinationData() {
        return combinationData;
    }

    public void setCombinationData(CombinationData combinationData) {
        this.combinationData = combinationData;
    }

    public EvaluationData getEvaluationData() {
        return evaluationData;
    }

    public void setEvaluationData(EvaluationData evaluationData) {
        this.evaluationData = evaluationData;
    }
}
