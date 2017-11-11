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
    public void playAsCodemaker(Match match) {
        setAllElements(match, codeElement);
        match.commitMove();
    }

    @Override
    public void playAsCodebreaker(Match match) {
        while (match.isCodebrekerTurn()) {
            setAllElements(match, trialElement);
            match.commitMove();
        }
    }

    private void setAllElements(Match match, int e) {
        IntStream.range(0, match.getConfig().getSlotCount())
                .forEach(i -> match.setElement(i, e));
    }
}
