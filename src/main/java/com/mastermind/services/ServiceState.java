package com.mastermind.services;

import com.mastermind.model.entities.types.Match;
import com.mastermind.model.entities.types.MatchConfig;
import com.mastermind.model.entities.types.Player;
import com.mastermind.model.persistence.RepositoryManager;

public class ServiceState {
    private Match activeMatch = new Match();
    private Player loggedInPlayer;
    private MatchConfig enviromentConfig = new MatchConfig();

    public Match getActiveMatch() {
        return activeMatch;
    }

    public void setActiveMatch(Match activeMatch) {
        this.activeMatch = activeMatch;
    }

    public void saveActiveMatch(){
        activeMatch = RepositoryManager.getMatchRepository().save(activeMatch);
    }

    public Player getLoggedInPlayer() {
        return loggedInPlayer;
    }

    public void setLoggedInPlayer(Player loggedInPlayer) {
        this.loggedInPlayer = loggedInPlayer;
    }

    public MatchConfig getEnviromentConfig() {
        return enviromentConfig;
    }

    public void setEnviromentConfig(MatchConfig enviromentConfig) {
        this.enviromentConfig = enviromentConfig;
    }
}
