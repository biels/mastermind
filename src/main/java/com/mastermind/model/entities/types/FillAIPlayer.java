package com.mastermind.model.entities.types;

import java.util.stream.IntStream;

public class FillAIPlayer extends AIPlayer {
    private int codeElement;
    private int trialElement;

    public FillAIPlayer() {
        this(0, 1);
    }

    public FillAIPlayer(int codeElement, int trialElement) {
        super(String.format("fill-ai-%d-%d", codeElement, trialElement));
        this.codeElement = codeElement;
        this.trialElement = trialElement;
    }

    @Override
    public void playAsCodemaker(Round match) {
        setAllElements(match, codeElement);
        match.commitMove();
    }

    @Override
    public void playAsCodebreaker(Round round) {
        while (round.hasNextTrial()) {
            setAllElements(round, trialElement);
            round.commitMove();
        }
    }

    private void setAllElements(Round round, int e) {
        MatchConfig config = round.getMatch().getConfig();
        IntStream.range(0, config.getSlotCount())
                .forEach(i -> round.setElement(i, e));
    }
}
