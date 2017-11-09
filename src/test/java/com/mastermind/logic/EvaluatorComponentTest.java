package com.mastermind.logic;

import com.mastermind.model.entities.types.Combination;
import com.mastermind.model.entities.types.TrialEvaluation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EvaluatorComponentTest {
    EvaluatorComponent evaluatorComponent;

    @BeforeEach
    void setUp() {
        evaluatorComponent = ComponentManager.getEvaluatorComponent();
    }

    @Test
    void evaluate1() {
        assertEquals(new TrialEvaluation(3, 0),
                evaluatorComponent.evaluate(
                        new Combination(0, 0, 1, 1),
                        new Combination(0, 0, 0, 1),
                        2
                ));
    }

    @Test
    void evaluate2() {
        assertEquals(new TrialEvaluation(0, 2),
                evaluatorComponent.evaluate(
                        new Combination(1, 1, 0, 0),
                        new Combination(0, 0, 1, 1),
                        2
                ));
    }

    @Test
    void evaluate3() {
        assertEquals(new TrialEvaluation(0, 2),
                evaluatorComponent.evaluate(
                        new Combination(0, 1, 2, 2),
                        new Combination(2, 0, 1, 3),
                        4
                ));
    }

    @Test
    void evaluate4() {
        assertEquals(new TrialEvaluation(1, 1),
                evaluatorComponent.evaluate(
                        new Combination(0, 1, 1),
                        new Combination(2, 0, 1),
                        3
                ));
    }

    @Test
    void evaluate5() {
        assertEquals(new TrialEvaluation(4, 3),
                evaluatorComponent.evaluate(
                        new Combination(0, 6, 1, 4, 4, 3, 1, 7, 2),
                        new Combination(6, 1, 1, 4, 0, 2, 7, 7, 2),
                        8
                ));
    }
}