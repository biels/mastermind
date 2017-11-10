package com.mastermind.logic;

import com.mastermind.model.entities.types.Match;
import com.mastermind.model.entities.types.TrialEvaluation;

public class RandomAlgorithmComponent extends AlgorithmComponent {
    @Override
    public void playAsCodemaker(Match match) {
        match.setElement(0, 0);
        match.setElement(1, 0);
        match.setElement(2, 1);
        match.setElement(3, 0);
        match.commitTrial();
    }

    @Override
    public void playAsCodebreaker(Match match) {
        while (match.isCodebrekerTurn()) {
            TrialEvaluation lastCommittedTrialEvaluation = match.getLastCommittedTrialEvaluation();
            match.setElement(0, 0);
            match.setElement(1, 1);
            match.setElement(2, 2);
            match.setElement(3, 0);
            match.commitTrial();
        }
    }
}
