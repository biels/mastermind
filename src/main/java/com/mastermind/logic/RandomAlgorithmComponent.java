package com.mastermind.logic;

import com.mastermind.model.entities.types.Match;
import com.mastermind.model.entities.types.MatchConfig;
import com.mastermind.model.entities.types.Round;

import java.util.Random;
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
    private void setAllElementsRandomly(Round round) {
        MatchConfig config = round.getMatch().getConfig();
        IntStream.range(0, config.getSlotCount())
                .forEach(i -> round.setElement(i, random.nextInt(config.getColorCount())));
    }
}
