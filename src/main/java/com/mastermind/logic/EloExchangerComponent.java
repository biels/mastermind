package com.mastermind.logic;

import com.mastermind.logic.types.Pair;

public class EloExchangerComponent {
    public Pair<Double, Double> calculateExchangeAmount(double elo1, double elo2, int winner, double K, boolean absolute){
        double R1 = Math.pow(10.0D, elo1 / 400.0D);
        double R2 = Math.pow(10.0D, elo2 / 400.0D);
        double E1 = R1 / (R1 + R2);
        double E2 = R2 / (R1 + R2);
        double S1 = winner == 1 ? 1.0D : (winner == 0 ? 0.5D : 0.0D);
        double S2 = winner == 2 ? 1.0D : (winner == 0 ? 0.5D : 0.0D);
        double r1 = (absolute ? elo1 : 0.0D) + K * (S1 - E1);
        double r2 = (absolute ? elo2 : 0.0D) + K * (S2 - E2);
        return new Pair<>(r1, r2);
    }
}
