package com.mastermind.services.ranking;

import com.mastermind.model.entities.types.HumanPlayer;
import com.mastermind.model.entities.types.MinimaxAIPlayer;
import com.mastermind.model.entities.types.Player;
import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.PlayerRepository;
import com.mastermind.model.persistence.repositories.impl.RepositoriesImpl;
import com.mastermind.services.ServiceManager;
import com.mastermind.services.ranking.responses.ListPositionsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class RankingServiceTest {
    List<Player> mockPlayers = new ArrayList<>();
    RankingService rankingService;

    @BeforeEach
    void setUp() {
        attachMockRepositoryWith();
        ServiceManager.restart();
        rankingService = ServiceManager.getRankingService();
    }

    @Test
    void listPositions() {
        Player p1 = mock(HumanPlayer.class);
        when(p1.getName()).thenReturn("human1");
        when(p1.getElo()).thenReturn(1250D);
        Player p2 = mock(HumanPlayer.class);
        when(p2.getName()).thenReturn("human2");
        when(p2.getElo()).thenReturn(1200D);
        Player p3 = mock(MinimaxAIPlayer.class);
        when(p3.getName()).thenReturn("minimax-ai-1");
        when(p3.getElo()).thenReturn(1240D);
        setMockPlayers(p1, p2, p3);
        ListPositionsResponse response = rankingService.listPositions();
        assertEquals(3, response.getRankingRows().size());
        assertEquals("human1", response.getRankingRows().get(0).getPlayer());
        assertEquals("minimax-ai-1", response.getRankingRows().get(1).getPlayer());
        assertEquals("human2", response.getRankingRows().get(2).getPlayer());
        assertEquals(1250D, (double)response.getRankingRows().get(0).getElo());
        assertEquals(1240D, (double)response.getRankingRows().get(1).getElo());
        assertEquals(1200D, (double)response.getRankingRows().get(2).getElo());
        assertEquals(1, response.getRankingRows().get(0).getPlace());
        assertEquals(2, response.getRankingRows().get(1).getPlace());
        assertEquals(3, response.getRankingRows().get(2).getPlace());
    }

    private void setMockPlayers(Player... players) {
        mockPlayers.clear();
        mockPlayers.addAll(Arrays.asList(players));
    }

    private void attachMockRepositoryWith() {
        RepositoryManager.attachImplementation(new RepositoriesImpl() {
            @Override
            public PlayerRepository getPlayerRepository() {
                return new PlayerRepository() {
                    @Override
                    public Optional<Player> findByName(String name) {
                        return Optional.empty();
                    }

                    @Override
                    public Optional<Player> findByMatch(Long matchId) {
                        return Optional.empty();
                    }

                    @Override
                    public List<Player> findAllByAITrue() {
                        return null;
                    }

                    @Override
                    public <S extends Player> S save(S entity) {
                        return null;
                    }

                    @Override
                    public void delete(Player entity) {

                    }

                    @Override
                    public Optional<Player> findOne(Long primaryKey) {
                        return Optional.empty();
                    }

                    @Override
                    public List<Player> findAll() {
                        return mockPlayers;
                    }

                    @Override
                    public Long count() {
                        return null;
                    }

                    @Override
                    public boolean exists(Long primaryKey) {
                        return false;
                    }
                };
            }
        });
    }
}