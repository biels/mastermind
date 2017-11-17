package com.mastermind.services.game.responses.exceptions;

import com.mastermind.model.entities.types.Match;
import com.mastermind.model.entities.types.Player;

import java.text.MessageFormat;

public class MatchNotYoursException extends RuntimeException {
    public MatchNotYoursException(Match match, Player actual) {
        super(MessageFormat.format("Match''s local player is {0} but you are {1}", match.getLocalPlayer().getName(), actual.getName()));
    }
}
