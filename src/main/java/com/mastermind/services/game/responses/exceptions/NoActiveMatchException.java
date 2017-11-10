package com.mastermind.services.game.responses.exceptions;

public class NoActiveMatchException extends RuntimeException {
    public NoActiveMatchException() {
        super("A currently active match is required to perform this operation");
    }
}
