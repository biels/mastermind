package com.mastermind.model.entities.types;

import com.mastermind.model.entities.base.Entity;
import com.mastermind.model.persistence.RepositoryManager;

import java.util.List;

/**
 * Represents a player in the system
 */
public abstract class Player extends Entity {
    private String name;
    private Double elo;

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

    public List<Match> getMatches() {
        if (matches == null) matches = RepositoryManager.getMatchRepository().findByPlayer(getId());
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

}
