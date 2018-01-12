package com.mastermind.model.persistence.repositories.impl;


import com.mastermind.model.persistence.repositories.impl.inmemory.MatchRepositoryInMemoryImpl;
import com.mastermind.model.persistence.repositories.impl.inmemory.PlayerRepositoryInMemoryImpl;

public class RepositoriesInMemoryImpl extends RepositoriesImpl {
    public RepositoriesInMemoryImpl() {
        playerRepository = new PlayerRepositoryInMemoryImpl();
        matchRepository = new MatchRepositoryInMemoryImpl();
    }
}
