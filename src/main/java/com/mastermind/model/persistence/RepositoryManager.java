package com.mastermind.model.persistence;

import com.mastermind.model.persistence.repositories.MatchRepository;

public class RepositoryManager {

    // TODO Getters for repository instances
    static MatchRepository matchRepository;

    private RepositoryManager() {
    }



    public static MatchRepository getMatchRepository() {
        return matchRepository;
    }

    public static void setMatchRepository(MatchRepository matchRepository) {
        RepositoryManager.matchRepository = matchRepository;
    }
}
