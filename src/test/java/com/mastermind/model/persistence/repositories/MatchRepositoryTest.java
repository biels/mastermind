package com.mastermind.model.persistence.repositories;

import com.mastermind.model.entities.base.Entity;
import com.mastermind.model.entities.types.*;
import com.mastermind.model.persistence.repositories.impl.inmemory.PlayerRepositoryInMemoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public abstract class MatchRepositoryTest<T extends MatchRepository> extends CrudRepositoryTest<T>{
    PlayerRepositoryInMemoryImpl playerRepository;
    Player p0, p1, p2, p3;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        playerRepository = new PlayerRepositoryInMemoryImpl();
    }

    @Override
    protected List<Entity> getSampleEntities() {
        p1 = playerRepository.save(new HumanPlayer());
        p0 = playerRepository.save(new HumanPlayer());
        p2 = playerRepository.save(new MinimaxAIPlayer());
        p3 = playerRepository.save(new RandomAIPlayer());
        Match p0vsp2 = touchMatch(new Match(p0, p2), false);
        Match p0vsp3 = touchMatch(new Match(p0, p3), false);
        Match p1vsp3 = touchMatch(new Match(p1, p3), false);
        Match p1vsp3F = touchMatch(new Match(p1, p3), true);
        Match p1vsp2F = touchMatch(new Match(p1, p2), true);
        return Arrays.asList(
                p0vsp2,
                p0vsp3,
                p1vsp3,
                p1vsp3F,
                p1vsp2F
        );
    }
    Match touchMatch(Match match, boolean finish){
        while (!match.isFinished()){
            int e = 0;
            IntStream.range(0, match.getConfig().getSlotCount())
                    .forEach(i -> match.setElement(i, e));
            match.commitMove();
            if(!finish)break;
        }
        return match;
    }
    @Test
    void findByPlayer() {
        List<Match> byId0 = component.findByPlayer(0L);
        assertEquals(0, byId0.size());
        saveAllSampleEntities();
        List<Match> byP0 = component.findByPlayer(p0.getId());
        assertEquals(2, byP0.size());
        assertEquals(p0, byP0.get(0).getLocalPlayer());
        List<Match> byP1 = component.findByPlayer(p1.getId());
        assertEquals(3, byP1.size());
        List<Match> byP2 = component.findByPlayer(p2.getId());
        assertEquals(2, byP2.size());
        assertEquals(p2, byP2.get(0).getEnemyPlayer());
        List<Match> byP3 = component.findByPlayer(p3.getId());
        assertEquals(3, byP3.size());
        assertEquals(p3, byP3.get(0).getEnemyPlayer());

    }

    @Test
    void findByPlayerAndFinishedFalse() {
        saveAllSampleEntities();
        List<Match> byP0 = component.findByPlayerAndFinishedFalse(p0.getId());
        assertEquals(2, byP0.size());
        assertEquals(p0, byP0.get(0).getLocalPlayer());
        List<Match> byP1 = component.findByPlayerAndFinishedFalse(p1.getId());
        assertEquals(1, byP1.size());
        List<Match> byP2 = component.findByPlayerAndFinishedFalse(p2.getId());
        assertEquals(1, byP2.size());
        assertEquals(p2, byP2.get(0).getEnemyPlayer());
        List<Match> byP3 = component.findByPlayerAndFinishedFalse(p3.getId());
        assertEquals(2, byP3.size());
        assertEquals(p3, byP3.get(0).getEnemyPlayer());
    }
}
