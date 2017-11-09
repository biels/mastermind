package com.mastermind.services.ranking;

import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.MatchRepository;
import com.mastermind.services.Service;
import com.mastermind.services.ranking.responses.ListPositionsResponse;

/**
 * Service that takes care of actions performed from the ranking window
 */
public class RankingService implements Service {
    private MatchRepository matchRepository = RepositoryManager.getMatchRepository();

    /**
     * Lists the positions in the ranking
     *
     * @return The list of positions in the ranking
     */
    public ListPositionsResponse listPositions() {
        ListPositionsResponse response = new ListPositionsResponse();

        return response;
    }

    /**
     * Clears all the positions in the ranking.
     * This action should require confirmation.
     */
    public void resetRanking() {

    }
}
