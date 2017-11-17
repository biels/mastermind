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
        boolean[] visited = new boolean[slotCount];
        for (int i = 0; i < slotCount; i++) {
            Integer codeElement = code.getElements().get(i);
            Integer trialElement = trial.getElements().get(i);
            visited[i] = codeElement.equals(trialElement);
            if (visited[i]) correctPlaceAndColorCount++;
        }
//        for (int i = 0; i < slotCount; i++) {
//            if(visited[i]) continue;
//            Integer codeElement = code.getElements().get(i);
//            for (int j = 0; j < slotCount; j++) {
//                if(visited[j]) continue;
//                Integer trialElement = trial.getElements().get(j);
//                if(codeElement.equals(trialElement))
//            }
//        }



        Function<Combination, List<Integer>> combinationCount = (Combination c) -> {
            List<Integer> elementCount = new ArrayList<>(Collections.nCopies(colorCount, 0));
            List<Integer> elements = c.getElements();
            for (int i = 0; i < elements.size(); i++) {
                if(visited[i])continue;
                Integer element = elements.get(i);
                elementCount.set(element, elementCount.get(element) + 1);
            }
            return elementCount;
        };
        List<Integer> codeElementsCount = combinationCount.apply(code);
        List<Integer> combinationElementsCount = combinationCount.apply(trial);
        for (int i = 0; i < colorCount; i++) {
            if (combinationElementsCount.get(i) == 0) continue;
            correctColorCount += Math.min(codeElementsCount.get(i), combinationElementsCount.get(i));
        }
        return new TrialEvaluation(correctPlaceAndColorCount, correctColorCount);
    }
}
