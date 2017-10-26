package com.mastermind.model.entities.types;

import com.mastermind.model.entities.base.Entity;
import com.mastermind.model.persistence.RepositoryManager;

import java.util.List;

/**
 * Represents a game betweeen a player and the AI
 */
public class Match extends Entity {
    private boolean finished; //Unfinished games are saved games
    private List<Round> rounds;
    // Config fields

    // End config fields
    private Player player;

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    public Player getPlayer() {
        if(player == null)
            player = RepositoryManager.getPlayerRepository()
                .findByMatch(getId()).get();
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
