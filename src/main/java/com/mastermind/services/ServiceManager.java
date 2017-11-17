package com.mastermind.services;

import com.mastermind.services.game.GameService;
import com.mastermind.services.login.LoginService;
import com.mastermind.services.players.PlayersService;
import com.mastermind.services.ranking.RankingService;

public class ServiceManager {
    private static PlayersService playersService = new PlayersService();
    private static GameService gameService = new GameService();
    private static LoginService loginService = new LoginService();
    private static RankingService rankingService = new RankingService();
    private static ServiceState state;

    public static PlayersService getPlayersService() {
        return playersService;
    }

    public static GameService getGameService() {
        return gameService;
    }

    public static LoginService getLoginService() {
        return loginService;
    }

    public static RankingService getRankingService() {
        return rankingService;
    }

    public static ServiceState getState() {
        if (state == null)
            initState();
        return state;
    }

    public static void setState(ServiceState state) {
        ServiceManager.state = state;
    }

    public static void initState() {
        ServiceManager.state = new ServiceState();
    }
    public static void restart(){
        playersService = new PlayersService();
        gameService = new GameService();
        loginService = new LoginService();
        rankingService = new RankingService();

        initState();
    }
}
