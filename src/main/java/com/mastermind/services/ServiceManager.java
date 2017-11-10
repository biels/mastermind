package com.mastermind.services;

import com.mastermind.services.game.GameService;
import com.mastermind.services.login.LoginService;
import com.mastermind.services.players.PlayersService;

public class ServiceManager {
    private static PlayersService playersService = new PlayersService();
    private static GameService gameService = new GameService();
    private static LoginService loginService = new LoginService();
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
        initState();
    }
}
