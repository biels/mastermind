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

    public void incrementElo(Double amount) {
        elo += amount;
        eloKHint = Math.max(eloKHint * 0.9, 20D);
    }

    public Double getEloKHint() {
        return eloKHint;
    }

    public void setEloKHint(Double eloKHint) {
        this.eloKHint = eloKHint;
    }

    public List<Match> getMatches() {
        if (matches == null) matches = RepositoryManager.getMatchRepository().findByPlayer(getId());
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    protected abstract void deserializeSpecific(String specific);
    protected abstract String serializeSpecific();

    public String serialize() {
        String type = getClass().getSimpleName();
        String s2 = "-";
        return type + s2 + // 0
                name + s2 + // 1
                Double.toString(elo) + s2 + // 2
                Double.toString(eloKHint) + s2 + // 3
                serializeSpecific(); // 4

    }

    public static Player deserialize(String s) {
        String s2 = "-";
        String[] split = s.split(s2);
        String type = split[0];
        String name = split[1];
        Double elo = Double.parseDouble(split[2]);
        Double eloKHint = Double.parseDouble(split[3]);
        String specific = split[4];
        Player result = null;
        if(type.equals("MinimaxAIPlayer")){
            result = new MinimaxAIPlayer(name, 0);
        }
        if(type.equals("HumanPlayer")){
            result = new HumanPlayer(name, "");
        }
        if(type.equals("RandomAIPlayer")){
            result = new RandomAIPlayer(name, 0L);
        }
        if(type.equals("FillAIPlayer")){
            result = new FillAIPlayer();
        }
        if(result != null){
            result.setName(name);
            result.setElo(elo);
            result.setEloKHint(eloKHint);
            result.deserializeSpecific(specific);
        }
        return result;
    }
}
