package com.mastermind.model.persistence.repositories.impl.inmemory;

import com.mastermind.model.entities.types.Match;
import com.mastermind.model.entities.types.Player;
import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.PlayerRepository;

import java.util.Optional;

public class PlayerRepositoryInMemoryImpl extends CrudRepositoryInMemoryImpl<Player> implements PlayerRepository {
    @Override
    public Optional<Player> findByName(String name) {
        return collection.stream().filter(player -> player.getName().equals(name)).findFirst();
    }

    @Override
    public Optional<Player> findByMatch(Long matchId) {
        Match match = RepositoryManager.getMatchRepository().findOne(matchId);
        if(match == null) return Optional.empty();
        return Optional.of(match.getPlayer());
    }
}
