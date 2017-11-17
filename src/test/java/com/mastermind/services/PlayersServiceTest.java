package com.mastermind.services;

import com.mastermind.model.entities.types.HumanPlayer;
import com.mastermind.model.entities.types.MinimaxAIPlayer;
import com.mastermind.model.entities.types.RandomAIPlayer;
import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.impl.RepositoriesInMemoryImpl;
import com.mastermind.services.players.PlayersService;
import com.mastermind.services.players.responses.CreatePlayerResponse;
import com.mastermind.services.players.responses.ListPlayersResponse;
import com.mastermind.services.players.responses.types.PlayerRowData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayersServiceTest {
    PlayersService service;

    @BeforeEach
    void setUp() {
        RepositoryManager.attachImplementation(new RepositoriesInMemoryImpl());
        service = new PlayersService();
    }

    @Test
    void listPlayersEmpty() {
        ListPlayersResponse response = service.listPlayers();
        List<PlayerRowData> playerRows = response.getPlayerRows();
        assertNotNull(playerRows);
        assertEquals(0, playerRows.size());
    }

    @Test
    void createHumanPlayerCorrect() {
        String name = "human-player-name";
        String password = "1234";
        CreatePlayerResponse<HumanPlayer> response = service.createHumanPlayer(name, password);
        assertTrue(response.isSuccess());
        HumanPlayer createdPlayer = response.getCreatedPlayer();
        assertNotNull(createdPlayer);
        assertNotNull(createdPlayer.getId());
        assertEquals(name, createdPlayer.getName());
        assertEquals(password, createdPlayer.getPassword());
    }

    @Test
    void createHumanPlayerIncorrect() {
        CreatePlayerResponse<HumanPlayer> response = service.createHumanPlayer("name", "123");
        assertFalse(response.isSuccess());
        assertNull(response.getCreatedPlayer());
    }

    @Test
    void createRandomAIPlayerCorrect() {
        String name = "random-ai-name";
        Long seed = 0x5593F3L;
        CreatePlayerResponse<RandomAIPlayer> response = service.createRandomAIPlayer(name, seed);
        assertTrue(response.isSuccess());
        RandomAIPlayer createdPlayer = response.getCreatedPlayer();
        assertNotNull(createdPlayer);
        assertNotNull(createdPlayer.getId());
        assertEquals(name, createdPlayer.getName());
        assertEquals(seed, createdPlayer.getSeed());
    }

    @Test
    void createMinimaxAIPlayerCorrect() {
        String name = "name";
        int depth = 12;
        CreatePlayerResponse<MinimaxAIPlayer> response = service.createMinmiaxAIPlayer(name, depth);
        assertTrue(response.isSuccess());
        MinimaxAIPlayer createdPlayer = response.getCreatedPlayer();
        assertNotNull(createdPlayer);
        assertNotNull(createdPlayer.getId());
        assertEquals(name, createdPlayer.getName());
        assertEquals(depth, createdPlayer.getDepth());

    }

    @Test
    void createMinimaxAIPlayerIncorrect() {
        String name = "name";
        int depth = 15;
        CreatePlayerResponse<MinimaxAIPlayer> response = service.createMinmiaxAIPlayer(name, depth);
        assertFalse(response.isSuccess());
        assertNull(response.getCreatedPlayer());
    }

    @Test
    void useCaseCreateListRemove() {
        // Create
        CreatePlayerResponse<HumanPlayer> response = service.createHumanPlayer("human-player-name-2", "123456");
        assertTrue(response.isSuccess());
        // List
        assertEquals(1, service.listPlayers().getPlayerRows().size());
        // Remove
        service.removePlayer(0);
        //List
        assertEquals(0, service.listPlayers().getPlayerRows().size());
    }
}
