package com.mastermind.services.login;

import com.mastermind.model.entities.types.HumanPlayer;
import com.mastermind.model.entities.types.Player;
import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.PlayerRepository;
import com.mastermind.services.ServiceManager;
import com.mastermind.services.login.responses.LoginResponse;

import java.util.Optional;

public class LoginService {
    private PlayerRepository playerRepository = RepositoryManager.getPlayerRepository();

    public LoginResponse login(String username, String password) {
        LoginResponse response = new LoginResponse();
        Optional<Player> found = playerRepository.findByName(username);
        if (!found.isPresent()) {
            response.getMessages().add("Player " + username + " was not found.");
            response.setSuccess(false);
            return response;
        }
        Player player = found.get();
        if (!(player instanceof HumanPlayer)) {
            response.getMessages().add("You cannot login as " + username + " since it is not a human player.");
            response.setSuccess(false);
            return response;
        }
        HumanPlayer humanPlayer = (HumanPlayer) player;
        if (humanPlayer.getPassword().equals(password)) {
            ServiceManager.getState().setLoggedInPlayer(humanPlayer);
            response.setSuccess(true);
        } else {
            response.getMessages().add("Password incorrect.");
            response.setSuccess(false);
        }
        return response;
    }

    public String getLoggedInPlayerName() {
        if (ServiceManager.getState().getLoggedInPlayer() == null) return null;
        return ServiceManager.getState().getLoggedInPlayer().getName();
    }

    public boolean isLoggedIn() {
        return ServiceManager.getState().getLoggedInPlayer() != null;
    }

    public void logout() {
        ServiceManager.getState().setLoggedInPlayer(null);
    }
}
