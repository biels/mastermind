package com.mastermind.logic;


import com.mastermind.model.entities.types.Match;
import com.mastermind.model.entities.types.MatchConfig;
import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.MatchRepository;

// Not sure this is useful, but shows how to use RepositoryManager

/**
 * @deprecated Will not be used
 */
public class MatchFactoryComponent {
    private MatchRepository matchRepository = RepositoryManager.getMatchRepository();
    public Match getMatch(MatchConfig matchConfig){
        return matchRepository.save(new Match());
    }
}
