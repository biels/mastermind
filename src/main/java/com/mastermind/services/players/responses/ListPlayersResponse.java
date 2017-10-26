package com.mastermind.services.players.responses;

import com.mastermind.services.players.responses.types.PlayerRowData;

import java.util.ArrayList;
import java.util.List;

public class ListPlayersResponse {
    private List<PlayerRowData> playerRows = new ArrayList<>();

    public List<PlayerRowData> getPlayerRows() {
        return playerRows;
    }

    public void setPlayerRows(List<PlayerRowData> playerRows) {
        this.playerRows = playerRows;
    }
}
