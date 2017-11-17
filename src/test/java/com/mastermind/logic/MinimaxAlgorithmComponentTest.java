package com.mastermind.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MinimaxAlgorithmComponentTest {
    MinimaxAlgorithmComponent component;

    @BeforeEach
    void setUp() {
        component = ComponentManager.getMinimaxAlgorithmComponent();
    }

    @Test
    void getAllPossibilities() {
        ArrayList<int[]> list = component.getAllPossibilities(4, 4, true);
        list.stream()
                .map(Arrays::toString)
                .forEach(System.out::println);
        System.out.println(list.size());
    }
}