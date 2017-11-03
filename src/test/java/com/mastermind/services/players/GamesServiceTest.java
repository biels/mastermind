package com.mastermind.services.players;

import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.impl.RepositoriesInMemoryImpl;
import com.mastermind.services.game.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class GamesServiceTest {
    GameService service;
    @BeforeEach
    void setUp() {
        RepositoryManager.attatchImplementation(new RepositoriesInMemoryImpl());
        service = new GameService();
    }
    @Test
    void newGame(){
        service.newGame();
    }
    @Test
    @Disabled
    void playSampleGame(){
        service.newGame();

    }
}
