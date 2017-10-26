package com.mastermind.model.persistence.repositories;

import com.mastermind.model.entities.base.Entity;
import com.mastermind.model.entities.types.Player;
import com.mastermind.model.persistence.repositories.types.CrudRepository;
import com.mastermind.model.persistence.repositories.types.ReadOnlyRepository;

import java.util.Optional;

public interface PlayerRepository extends CrudRepository<Player> {
    Optional<Player> findByName(String name);
    Optional<Player> findByMatch(Long matchId);
}
