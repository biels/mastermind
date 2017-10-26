package com.mastermind.services.game.responses;

import com.mastermind.services.game.responses.types.SavedGameRowData;

import java.util.List;

public class ListSavedGamesResponse {
    private List<SavedGameRowData> savedGameRows;


    public List<SavedGameRowData> getSavedGameRows() {
        return savedGameRows;
    }

    public void setSavedGameRows(List<SavedGameRowData> savedGameRows) {
        this.savedGameRows = savedGameRows;
    }
}
