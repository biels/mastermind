package com.mastermind.model.persistence.repositories;

import com.mastermind.model.persistence.repositories.types.Repository;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public abstract class RepositoryTest<T extends Repository> {
    protected T component;

    public abstract T getInstance();

    @BeforeEach
    void setUp() {
        component = getInstance();
        assertNotNull(component, this.getClass().getSimpleName() + " getInstance() is not configured");
    }
}
