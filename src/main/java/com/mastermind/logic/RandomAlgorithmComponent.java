package com.mastermind.logic;

import com.mastermind.model.entities.types.Match;
import com.mastermind.model.entities.types.MatchConfig;
import com.mastermind.model.entities.types.Round;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomAlgorithmComponent extends AlgorithmComponent {
    Random random = new Random();
    @Override
    public void playAsCodemaker(Round round) {
        setAllElementsRandomly(round);
        round.commitMove();
    }

    @Override
    public void playAsCodebreaker(Round round) {
        while (round.hasNextTrial()) {
            setAllElementsRandomly(round);
            round.commitMove();
        }
    }
    public void setAllElementsRandomly(Round round) {
        MatchConfig config = round.getMatch().getConfig();
        IntStream ints = random
                .ints(config.getSlotCount()+20,0, config.getColorCount());
        if(!round.getMatch().getConfig().isAllowRepetition()) ints = ints.distinct();
        List<Integer> collect = ints.boxed().collect(Collectors.toList());
        IntStream.range(0, config.getSlotCount())
                .forEach(i -> round.setElement(i, collect.get(i)));
    }
}
