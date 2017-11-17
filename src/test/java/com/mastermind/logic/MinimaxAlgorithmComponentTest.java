package com.mastermind.logic;

import com.mastermind.model.entities.types.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

class MinimaxAlgorithmComponentTest {
    MinimaxAlgorithmComponent component;

    @BeforeEach
    void setUp() {
        component = ComponentManager.getMinimaxAlgorithmComponent();
    }

    @Test
    void getAllPossibilitiesRepetition() {
        ArrayList<int[]> list = component.getAllPossibilities
                (2, 4, true);
//        list.stream()
//                .map(Arrays::toString)
//                .forEach(System.out::println);
//        System.out.println(list.size());
    }

    @Test
    void getAllPossibilitiesNoRepetition() {
        ArrayList<int[]> list = component.getAllPossibilities
                (3, 6, false);
//        list.stream()
//                .map(Arrays::toString)
//                .forEach(System.out::println);
//        System.out.println(list.size());

    }
    @Test
    void playAsCodebreaker1() {
        playAsCodebreaker(new Combination(1, 2, 3, 4, 0), false, 7, 5);
    }
    @Test
    void playAsCodebreaker2() {
        playAsCodebreaker(new Combination(1, 4, 4, 4, 0), true, 7, 5);
    }
    @Test
    void playAsCodebreaker3() {
        playAsCodebreaker(new Combination(1, 4, 4, 4, 0), true, 8, 5);
    }

    void playAsCodebreaker(Combination code, boolean allowRepetition, int maxTrialsToWin, int colorCount) {
        Match match = mock(Match.class);
        MatchConfig config = new MatchConfig();
        config.setAllowRepetition(allowRepetition);
        config.setLocalStartsMakingCode(true);
        config.setMaxTrialCount(maxTrialsToWin);
        config.setColorCount(colorCount);
        config.setSlotCount(code.getSize());
        when(match.getConfig()).thenReturn(config);
        Round round = new Round(match, new HumanPlayer(), new MinimaxAIPlayer());
        IntStream.range(0, config.getSlotCount())
                .forEach(i -> round.setElement(i, code.getElements().get(i)));
        round.commitMove();
        round.getTrials().stream()
                .map(Trial::toString)
                .forEachOrdered(System.out::println);
        int trialsUsed = round.getTrials().size();
        System.out.println("Trials: " + trialsUsed + "/" + maxTrialsToWin);
        Assertions.assertTrue(trialsUsed < maxTrialsToWin,
                "It took " + trialsUsed + " to win, but was expected to win in at most " + maxTrialsToWin + " trials");
    }
}