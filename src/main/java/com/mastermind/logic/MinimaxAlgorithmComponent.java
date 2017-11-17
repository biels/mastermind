package com.mastermind.logic;

import com.mastermind.model.entities.types.Round;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MinimaxAlgorithmComponent extends AlgorithmComponent {


    @Override
    public void playAsCodemaker(Round match) {

    }

    @Override
    public void playAsCodebreaker(Round match) {
        //ArrayList<ArrayList<Integer>> possibilities = getAllPossibilities();

    }

    public ArrayList<int[]> getAllPossibilities(int length, int elements, boolean repetition) {
        Set<Integer> remaining = new HashSet<>();
        ArrayList<int[]> combinations = new ArrayList<>();
        permute(combinations, new int[length], elements, length, 0);
        return combinations;
    }

    private void permute(ArrayList<int[]> list, int[] tmp, int elements, int length, int l) {
        if (l == length)
            list.add(tmp.clone());
        else {
            for (int i = 0; i <= elements; i++) {
                tmp[l] = i;
                permute(list, tmp, elements, length, l + 1);
            }
        }
    }
}