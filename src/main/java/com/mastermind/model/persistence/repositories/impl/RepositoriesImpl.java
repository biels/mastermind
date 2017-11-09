package com.mastermind.model.persistence.repositories.impl;

import com.mastermind.model.persistence.repositories.MatchRepository;
import com.mastermind.model.persistence.repositories.PlayerRepository;

public abstract class RepositoriesImpl {
    MatchRepository matchRepository;
    PlayerRepository playerRepository;

    public MatchRepository getMatchRepository() {
        return matchRepository;
    }

    public PlayerRepository getPlayerRepository() {
        return playerRepository;
    }
}
