package com.mastermind.services.game.responses;

import com.mastermind.services.game.responses.types.EvaluationData;

public class CommitTrialResponse {

    private EvaluationData evaluation;
    private boolean gameEnded;

    public EvaluationData getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(EvaluationData evaluation) {
        this.evaluation = evaluation;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

}
