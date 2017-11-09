package com.mastermind.model.persistence.repositories;


import com.mastermind.model.entities.types.Match;
import com.mastermind.model.persistence.repositories.types.CrudRepository;

import java.util.List;

public interface MatchRepository extends CrudRepository<Match> {
    List<Match> findByPlayer(Long playerId);

    List<Match> findByPlayerAndFinishedFalse(Long playerId);
}
