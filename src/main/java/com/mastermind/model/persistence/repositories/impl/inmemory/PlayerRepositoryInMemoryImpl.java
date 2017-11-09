package com.mastermind.model.persistence.repositories.impl.inmemory;

import com.mastermind.model.entities.types.AIPlayer;
import com.mastermind.model.entities.types.Match;
import com.mastermind.model.entities.types.Player;
import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.PlayerRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlayerRepositoryInMemoryImpl extends CrudRepositoryInMemoryImpl<Player> implements PlayerRepository {
    @Override
    public Optional<Player> findByName(String name) {
        return collection.stream().filter(player -> player.getName().equals(name)).findFirst();
    }

    @Override
    public Optional<Player> findByMatch(Long matchId) {
        Optional<Match> match = RepositoryManager.getMatchRepository().findOne(matchId);
        return match.map(Match::getLocalPlayer);
    }

    @Override
    public List<Player> findAllByAITrue() {
        return findAll().stream()
                .filter(player -> player instanceof AIPlayer)
                .collect(Collectors.toList());
    }

}
