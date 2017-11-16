package com.mastermind.logic;

import com.mastermind.logic.types.Pair;
import com.mastermind.services.ServiceManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EloExchangerComponentTest {

    private EloExchangerComponent eloExchanger;

    @BeforeEach
    void setUp() {
        eloExchanger = ComponentManager.getEloExchangerComponent();
    }
    // Test cases from https://metinmediamath.wordpress.com/2013/11/27/how-to-calculate-the-elo-rating-including-example/
    @Test
    void calculateExchangeAmount1() {
        Pair<Double, Double> exchangeAmount = eloExchanger.calculateExchangeAmount
                (2400D, 2000D, 1, 32D, true);
        assertEquals(2403D, Math.round(exchangeAmount.getFirst()));
        assertEquals(1997D, Math.round(exchangeAmount.getSecond()));
    }
    @Test
    void calculateExchangeAmount2() {
        Pair<Double, Double> exchangeAmount = eloExchanger.calculateExchangeAmount
                (2400D, 2000D, 2, 32D, true);
        assertEquals(2371D, Math.round(exchangeAmount.getFirst()));
        assertEquals(2029D, Math.round(exchangeAmount.getSecond()));
    }
}