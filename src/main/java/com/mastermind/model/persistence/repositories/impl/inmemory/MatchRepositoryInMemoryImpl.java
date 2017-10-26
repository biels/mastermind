package com.mastermind.model.persistence.repositories.impl.inmemory;

import com.mastermind.model.entities.types.Match;
import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.MatchRepository;

import java.util.List;
import java.util.stream.Collectors;

public class MatchRepositoryInMemoryImpl extends CrudRepositoryInMemoryImpl<Match> implements MatchRepository {

    @Override
    public List<Match> findByPlayer(Long playerId) {
        return RepositoryManager.getMatchRepository().findAll().stream()
                .filter(match -> match.getPlayer().getId().equals(playerId))
                .collect(Collectors.toList());
    }
}
