package com.mastermind.model.persistence.repositories.impl.inmemory;

import com.mastermind.model.entities.types.Match;
import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.MatchRepository;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MatchRepositoryInMemoryImpl extends CrudRepositoryInMemoryImpl<Match> implements MatchRepository {

    @Override
    public List<Match> findByPlayer(Long playerId) {
        Predicate<Match> filterByPlayer = match ->
                match.getLocalPlayer().getId().equals(playerId) ||
                match.getEnemyPlayer().getId().equals(playerId);
        return findAll().stream()
                .filter(filterByPlayer)
                .collect(Collectors.toList());
    }

    @Override
    public List<Match> findByPlayerAndFinishedFalse(Long playerId) {
        return findByPlayer(playerId).stream()
                .filter(match -> !match.isFinished())
                .collect(Collectors.toList());
    }
}
