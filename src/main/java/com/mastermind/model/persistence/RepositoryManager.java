package com.mastermind.model.persistence;

import com.mastermind.model.persistence.repositories.MatchRepository;
import com.mastermind.model.persistence.repositories.PlayerRepository;
import com.mastermind.model.persistence.repositories.impl.RepositoriesImpl;
import com.mastermind.model.persistence.repositories.impl.inmemory.MatchRepositoryInMemoryImpl;

public class RepositoryManager {

    private static RepositoriesImpl repositories;

    private RepositoryManager() {
    }

    public static void attatchImplementation(RepositoriesImpl impl){
        RepositoryManager.repositories = impl;
    }

    public static MatchRepository getMatchRepository() {
        return repositories.getMatchRepository();
    }

    public static PlayerRepository getPlayerRepository() {
        return repositories.getPlayerRepository();
    }
}
