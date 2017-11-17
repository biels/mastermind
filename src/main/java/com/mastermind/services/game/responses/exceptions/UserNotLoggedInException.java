package com.mastermind.services.game.responses.exceptions;

public class UserNotLoggedInException extends RuntimeException {
    public UserNotLoggedInException() {
        super("User needs to be logged in to perform this operation");
    }
}
