package com.mastermind.services.players;

import com.mastermind.model.entities.types.HumanPlayer;
import com.mastermind.model.entities.types.Player;
import com.mastermind.model.entities.types.RandomAIPlayer;
import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.PlayerRepository;
import com.mastermind.services.players.responses.CreatePlayerResponse;

import java.text.MessageFormat;

/**
 * Service that takes care of actions performed from the players window
 */
public class PlayersService {
    private PlayerRepository playerRepository = RepositoryManager.getPlayerRepository();
    public CreatePlayerResponse<HumanPlayer> createHumanPlayer(String name, String password){
        CreatePlayerResponse<HumanPlayer> response = new CreatePlayerResponse<>();
        if (checkExistenceByName(name, response)) return response;
        if(password.length() < 4){
            response.setSuccess(false);
            response.getMessages().add("Password must have at leas 4 characters");
            return response;
        }

        HumanPlayer newPlayer = new HumanPlayer();
        newPlayer.setName(name);
        newPlayer.setPassword(password);
        response.setCreatedPlayer(playerRepository.save(newPlayer));
        response.setSuccess(true);
        return response;
    }

    public CreatePlayerResponse<RandomAIPlayer> createRandomAIPlayer(String name, Long seed){
        CreatePlayerResponse<RandomAIPlayer> response = new CreatePlayerResponse<>();
        if (checkExistenceByName(name, response)) return response;
        RandomAIPlayer newPlayer = new RandomAIPlayer();
        newPlayer.setName(name);
        newPlayer.setSeed(seed);
        return response;
    }

    private boolean checkExistenceByName(String name, CreatePlayerResponse<? extends Player> response) {
        if(playerRepository.findByName(name).isPresent()){
            response.setSuccess(false);
            response.getMessages().add(
                    MessageFormat.format("Player {0} already exists", name));
            return true;
        }
        return false;
    }
}
