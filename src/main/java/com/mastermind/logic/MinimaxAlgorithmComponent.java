package com.mastermind.logic;

import com.mastermind.model.entities.types.MatchConfig;
import com.mastermind.model.entities.types.Round;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MinimaxAlgorithmComponent extends AlgorithmComponent {


    @Override
    public void playAsCodemaker(Round match) {

    }

    @Override
    public void playAsCodebreaker(Round round) {
        MatchConfig config = round.getMatch().getConfig();
        ArrayList<int[]> possibilities = getAllPossibilities(config.getSlotCount(), config.getColorCount(), config.isAllowRepetition());

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
                if(used[i]) continue;
                boolean usedTmp[] = used.clone();
                usedTmp[i] = true;
                tmp[l] = i;
                combine(list, tmp, usedTmp, elements, length, l + 1);
            }
        }
    }
}