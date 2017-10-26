package com.mastermind.services.game;

import com.mastermind.services.game.responses.*;
import com.mastermind.services.game.responses.types.*;


import java.util.ArrayList;

/**
 * Service that takes care of actions performed from the game window
 */
public class GameService {

    /**
     * Called from a new game button
     */
    public void newGame(){

    }

    /**
     * Called from a restart game button
     * Restarts game with the same settings and combination
     */
    public void restartGame(){

    }

    /**
     * Places a color into the current uncomitted trial.
     * Called on drag & drop into the combination box.
     * @param colorId The identifier of the color to place
     * @param position The target position within the combination
     */
    public void placeColor(int colorId, int position){

    }

    /**
     * Copies the combiantion from the previous trial into the current one
     */
    public void duplicatePreviousTrial(){

    }

    /**
     * The action of comitting the current trial
     * @return The evaluations of the comitted trial and whether the game has finished or not
     */
    public CommitTrialResponse commitTrial(){
        CommitTrialResponse response = new CommitTrialResponse();

        // Sample evaluation
        EvaluationData evaluation = new EvaluationData();
        evaluation.setCorrectPlaceAndColorCount(1);
        evaluation.setCorrectColorCount(2);
        response.setEvaluation(evaluation);

        return response;
    }

    /**
     * List the saved games. To be displayed on a table to choose and load.
     * @return The list of saved games.
     */
    public ListSavedGamesResponse listSavedGames(){
        ListSavedGamesResponse response = new ListSavedGamesResponse();
        // Access repositories and fill response
        return response;
    }

    /**
     * Loads a game from the list of saved games, identified by its position on the list.
     * @param index The position of the game in the list of saved games.
     * @return The data needed to load the game and continue playing
     */
    public LoadGameResponse loadSavedGameFromList(int index){
        LoadGameResponse response = new LoadGameResponse();
        // Access repositories and fill response
        return response;
    }

    /**
     * Removes a game from the list of saved games, identified by its position on the list
     * @param index The position of the game in the list of saved games.
     */
    public void removeSavedGame(int index){

    }
}
