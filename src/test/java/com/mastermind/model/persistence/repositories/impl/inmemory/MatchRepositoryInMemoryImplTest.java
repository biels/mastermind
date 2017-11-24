package com.mastermind.model.persistence.repositories.impl.inmemory;

import com.mastermind.model.entities.base.Entity;
import com.mastermind.model.persistence.repositories.MatchRepositoryTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MatchRepositoryInMemoryImplTest extends MatchRepositoryTest<MatchRepositoryInMemoryImpl>{

    @Override
    public MatchRepositoryInMemoryImpl getInstance() {
        return new MatchRepositoryInMemoryImpl();
    }
}