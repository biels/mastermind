package com.mastermind.services.ranking;

import com.mastermind.model.entities.types.Player;
import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.PlayerRepository;
import com.mastermind.services.Service;
import com.mastermind.services.ranking.responses.ListPositionsResponse;
import com.mastermind.services.ranking.responses.types.RankingRowData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service that takes care of actions performed from the ranking window
 */
public class RankingService implements Service {
    private PlayerRepository playerRepository = RepositoryManager.getPlayerRepository();

    /**
     * Lists the positions in the ranking
     *
     * @return The list of positions in the ranking
     */
    public ListPositionsResponse listPositions() {
        ListPositionsResponse response = new ListPositionsResponse();
        List<Player> toSort = new ArrayList<>();
        for (Player player : playerRepository.findAll()) {
            toSort.add(player);
        }
        toSort.sort(Comparator.comparingDouble(Player::getElo).reversed());
        List<RankingRowData> list = new ArrayList<>();
        for (int i = 0; i < toSort.size(); i++) {
            Player player = toSort.get(i);
            RankingRowData rankingRowData = new RankingRowData(i + 1, player.getName(), player.getElo());
            list.add(rankingRowData);
        }
        response.setRankingRows(
                list
        );
        return response;
    }
}
