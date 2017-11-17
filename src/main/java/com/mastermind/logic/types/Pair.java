package com.mastermind.logic.types;

public class Pair<A, B> {
    A first = null;
    B second = null;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return this.first;
    }

    public void setFirst(A first) {
        this.first = first;
    }

    public B getSecond() {
        return this.second;
    }

    public void setSecond(B second) {
        this.second = second;
    }
}