package com.mastermind.logic;

import com.mastermind.model.entities.types.Combination;
import com.mastermind.model.entities.types.TrialEvaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class EvaluatorComponent {
    public TrialEvaluation evaluate(Combination code, Combination trial, int colorCount) {
        if (!code.isComplete()) throw new RuntimeException("Code must be a complete combination");
        if (!trial.isComplete()) throw new RuntimeException("Trial must be a complete combination");
        int slotCount = code.getSize();
        int correctPlaceAndColorCount = 0;
        int correctColorCount = 0;
        List<Integer> visited = new ArrayList<>();
        for (int i = 0; i < slotCount; i++) {
            Integer codeElement = code.getElements().get(i);
            Integer trialElement = trial.getElements().get(i);
            if (codeElement.equals(trialElement)) {
                visited.add(i);
                correctPlaceAndColorCount++;
            }
        }
        Function<Combination, List<Integer>> combinationCount = (Combination c) -> {
            List<Integer> elementCount = new ArrayList<>(Collections.nCopies(colorCount, 0));
            c.getElements().forEach(element -> elementCount.set(element, elementCount.get(element) + 1));
            return elementCount;
        };
        List<Integer> codeElementsCount = combinationCount.apply(code);
        List<Integer> combinationElementsCount = combinationCount.apply(trial);
        for (int i = 0; i < colorCount; i++) {
            if (combinationElementsCount.get(i) == 0) continue;
            if (combinationElementsCount.get(i).equals(codeElementsCount.get(i)))
                correctColorCount++;
        }
        return new TrialEvaluation(correctPlaceAndColorCount, correctColorCount);
    }
}
