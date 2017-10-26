package com.mastermind.model.persistence.repositories.impl;

import com.mastermind.model.persistence.repositories.impl.inmemory.MatchRepositoryInMemoryImpl;
import com.mastermind.model.persistence.repositories.impl.inmemory.PlayerRepositoryInMemoryImpl;

public class RepositoriesInMemoryImpl extends RepositoriesImpl {
    public RepositoriesInMemoryImpl() {
        matchRepository = new MatchRepositoryInMemoryImpl();
        playerRepository = new PlayerRepositoryInMemoryImpl();
    }
}
