package com.mastermind.logic;

import com.mastermind.model.entities.types.Combination;
import com.mastermind.model.entities.types.MatchConfig;
import com.mastermind.model.entities.types.Round;
import com.mastermind.model.entities.types.TrialEvaluation;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MinimaxAlgorithmComponent extends AlgorithmComponent {
    EvaluatorComponent evaluator = ComponentManager.getEvaluatorComponent();

    @Override
    public void playAsCodemaker(Round round) {
        MatchConfig config = round.getMatch().getConfig();
        int slotCount = config.getSlotCount();
        int colorCount = config.getColorCount();
        for (int i = 0; i < slotCount; i++) {
            round.setElement(i, i % colorCount);
        }
    }

    @Override
    public void playAsCodebreaker(Round round) {
        MatchConfig config = round.getMatch().getConfig();
        int slotCount = config.getSlotCount();
        int colorCount = config.getColorCount();
        boolean allowRepetition = config.isAllowRepetition();
        List<int[]> possibilities = getAllPossibilities(slotCount, colorCount, allowRepetition);
        boolean first = true;
        int[] guess = new int[slotCount];
        while (possibilities.size() > 1 && round.hasNextTrial()) {
            //Choose
            if (first) {
                first = false;
                for (int i = 0; i < slotCount; i++) {
                    guess[i] = i / (allowRepetition ? 2 : 1);
                }
            } else {
                guess = possibilities.get(possibilities.size() / 2);
            }
            //Play
            for (int i = 0; i < slotCount; i++) {
                round.setElement(i, guess[i]);
            }
            round.commitMove();
            //Filter
            TrialEvaluation trialEvaluation = round.getLastCommittedTrial().getTrialEvaluation();
            int[] finalGuess = guess;
            possibilities = possibilities.parallelStream()
                    .filter(c -> evaluator.evaluate(
                            new Combination(IntStream.of(c).boxed().toArray(Integer[]::new)),
                            new Combination(IntStream.of(finalGuess).boxed().toArray(Integer[]::new)),
                            colorCount
                            ).equals(trialEvaluation)
                    )
                    .collect(Collectors.toList());
        }
        guess = possibilities.get(0);
        if(possibilities.size() > 0){
            if(round.hasNextTrial()){
                for (int i = 0; i < slotCount; i++) {
                    round.setElement(i, guess[i]);
                }
                round.commitMove();
            }
        }else {
            throw new RuntimeException("Algorithm error");
        }
    }

    public ArrayList<int[]> getAllPossibilities(int length, int elements, boolean repetition) {
        ArrayList<int[]> combinations = new ArrayList<>();
        if (repetition) {
            permute(combinations, new int[length], elements, length, 0);
        } else {
            combine(combinations, new int[length], new boolean[elements], elements, length, 0);
        }
        return combinations;
    }

    private void permute(ArrayList<int[]> list, int[] tmp, int elements, int length, int l) {
        if (l == length)
            list.add(tmp.clone());
        else {
            for (int i = 0; i < elements; i++) {
                tmp[l] = i;
                permute(list, tmp, elements, length, l + 1);
            }
        }
    }

    private void combine(ArrayList<int[]> list, int[] tmp, boolean[] used, int elements, int length, int l) {
        if (l == length)
            list.add(tmp.clone());
        else {
            for (int i = 0; i < elements; i++) {
                if (used[i]) continue;
                boolean usedTmp[] = used.clone();
                usedTmp[i] = true;
                tmp[l] = i;
                combine(list, tmp, usedTmp, elements, length, l + 1);
            }
        }
    }
}