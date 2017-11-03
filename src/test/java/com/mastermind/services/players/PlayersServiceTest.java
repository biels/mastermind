package com.mastermind.services.players;

import com.mastermind.model.entities.types.HumanPlayer;
import com.mastermind.model.entities.types.MinimaxAIPlayer;
import com.mastermind.model.entities.types.RandomAIPlayer;
import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.impl.RepositoriesInMemoryImpl;
import com.mastermind.services.players.responses.CreatePlayerResponse;
import com.mastermind.services.players.responses.ListPlayersResponse;
import com.mastermind.services.players.responses.types.PlayerRowData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.beans.Transient;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayersServiceTest {
    PlayersService service;

    @BeforeEach
    void setUp() {
        RepositoryManager.attatchImplementation(new RepositoriesInMemoryImpl());
        service = new PlayersService();
    }

    @Test
    void listPlayersEmpty() {
        ListPlayersResponse response = service.listPlayers();
        List<PlayerRowData> playerRows = response.getPlayerRows();
        assertNotNull(playerRows);
        assertEquals(playerRows.size(), 0);
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
        assertEquals(createdPlayer.getName(), name);
        assertEquals(createdPlayer.getPassword(), password);
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
        assertEquals(createdPlayer.getName(), name);
        assertEquals(createdPlayer.getSeed(), seed);
    }

    @Test
    void createMinimaxAIPlayerCorrect(){
        String name = "name";
        int depth = 12;
        CreatePlayerResponse<MinimaxAIPlayer> response= service.createMinmiaxAIPlayer(name, depth);
        assertTrue(response.isSuccess());
        MinimaxAIPlayer createdPlayer = response.getCreatedPlayer();
        assertNotNull(createdPlayer);
        assertNotNull(createdPlayer.getId());
        assertEquals(createdPlayer.getName(), name);
        assertEquals(createdPlayer.getDepth(), depth);

    }
    @Test
    void createMinimaxAIPlayerIncorrect(){
        String name = "name";
        int depth = 15;
        CreatePlayerResponse<MinimaxAIPlayer> response= service.createMinmiaxAIPlayer(name, depth);
        assertFalse(response.isSuccess());
        assertNull(response.getCreatedPlayer());
    }
}
