package com.mastermind.logic;

import com.mastermind.model.entities.types.Combination;
import com.mastermind.model.entities.types.Round;

public abstract class AlgorithmComponent {
    abstract Combination nextTrial(Round round);
}
