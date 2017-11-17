package com.mastermind.logic;

public class ComponentManager {
    private static EvaluatorComponent evaluatorComponent;
    private static MinimaxAlgorithmComponent minimaxAlgorithmComponent;
    private static RandomAlgorithmComponent randomAlgorithmComponent;
    private static EloExchangerComponent eloExchangerComponent;

    public static EvaluatorComponent getEvaluatorComponent() {
        if (evaluatorComponent == null) evaluatorComponent = new EvaluatorComponent();
        return evaluatorComponent;
    }

    public static MinimaxAlgorithmComponent getMinimaxAlgorithmComponent() {
        if (minimaxAlgorithmComponent == null) minimaxAlgorithmComponent = new MinimaxAlgorithmComponent();
        return minimaxAlgorithmComponent;
    }

    public static RandomAlgorithmComponent getRandomAlgorithmComponent() {
        if (randomAlgorithmComponent == null) randomAlgorithmComponent = new RandomAlgorithmComponent();
        return randomAlgorithmComponent;
    }

    public static EloExchangerComponent getEloExchangerComponent(){
        if(eloExchangerComponent == null) eloExchangerComponent = new EloExchangerComponent();
        return eloExchangerComponent;
    }
}
