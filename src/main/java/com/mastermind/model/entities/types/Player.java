package com.mastermind.model.entities.types;

import com.mastermind.model.entities.base.Entity;
import com.mastermind.model.persistence.RepositoryManager;

import java.util.List;

/**
 * Represents a player in the system
 */
public abstract class Player extends Entity {
    private String name;
    private Double elo = 1200D;
    private Double eloKHint = 32D;

    private List<Match> matches;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getElo() {
        return elo;
    }

    public void setElo(Double elo) {
        this.elo = elo;
    }
    public void incrementElo(Double amount){
        elo += amount;
        eloKHint = Math.max(eloKHint * 0.9, 20D);
    }

    public Double getEloKHint() {
        return eloKHint;
    }

    public List<Match> getMatches() {
        if (matches == null) matches = RepositoryManager.getMatchRepository().findByPlayer(getId());
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

}
