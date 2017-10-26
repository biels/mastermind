package com.mastermind.services.ranking.responses;

import com.mastermind.services.ranking.responses.types.RankingRowData;

import java.util.List;

public class ListPositionsResponse {
    private List<RankingRowData> rankingRows;

    public List<RankingRowData> getRankingRows() {
        return rankingRows;
    }

    public void setRankingRows(List<RankingRowData> rankingRows) {
        this.rankingRows = rankingRows;
    }
}
