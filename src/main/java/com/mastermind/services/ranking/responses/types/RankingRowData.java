package com.mastermind.services.ranking.responses.types;

public class RankingRowData {
    private int place;
    private String player;
    private Double elo;

    public RankingRowData(int place, String player, Double elo) {
        this.place = place;
        this.player = player;
        this.elo = elo;
    }

    public int getPlace() {
        return place;
    }

    public String getPlayer() {
        return player;
    }

    public Double getElo() {
        return elo;
    }
}
