package com.mastermind.services.players;

import com.mastermind.model.entities.types.HumanPlayer;
import com.mastermind.model.entities.types.MinimaxAIPlayer;
import com.mastermind.model.entities.types.Player;
import com.mastermind.model.entities.types.RandomAIPlayer;
import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.PlayerRepository;
import com.mastermind.services.Service;
import com.mastermind.services.players.responses.CreatePlayerResponse;
import com.mastermind.services.players.responses.ListPlayersResponse;
import com.mastermind.services.players.responses.types.PlayerRowData;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service that takes care of actions performed from the players window
 */
public class PlayersService implements Service {
    private PlayerRepository playerRepository = RepositoryManager.getPlayerRepository();

    /**
     * Lists the players registered in the system
     * @return The list of players in the system
     */
    public ListPlayersResponse listPlayers(){
        ListPlayersResponse response = new ListPlayersResponse();
        getPlayerList().stream()
                .map(player -> {
                    PlayerRowData data = new PlayerRowData();
                    data.setName(player.getName());
                    data.setHuman(player instanceof HumanPlayer);
                    data.setType(player.getClass().getName());
                    return data;
                })
                .collect(Collectors.toList());
        return response;
    }

    /**
     * Removes a player from the list of players, identified by its position on the list
     * @param index The position of the player in the list.
     */
    public void removePlayer(int index){
        Player target = getPlayerList().get(index);
        playerRepository.delete(target);
    }

    private List<Player> getPlayerList() {
        return playerRepository.findAll();
    }

    /**
     * Creates a human player. Called from a registration form.
     * @param name The name of the player.
     * @param password The password used to secure this player's account.
     * @return The created player if the operation was successful, or a list containing error messages.
     */
    public CreatePlayerResponse<HumanPlayer> createHumanPlayer(String name, String password){
        CreatePlayerResponse<HumanPlayer> response = new CreatePlayerResponse<>();
        if (checkExistenceByName(name, response)) return response;
        if(password.length() < 4){
            response.setSuccess(false);
            response.getMessages().add("Password must have at least 4 characters");
            return response;
        }

        HumanPlayer newPlayer = new HumanPlayer();
        newPlayer.setName(name);
        newPlayer.setPassword(password);
        response.setCreatedPlayer(playerRepository.save(newPlayer));
        response.setSuccess(true);
        return response;
    }

    /**
     * Creates a random AI player. Called from an add AI player form.
     * @param name The name of the player.
     * @param seed The seed used by the random algorithm.
     * @return The created player if the operation was successful, or a list containing error messages.
     */
    public CreatePlayerResponse<RandomAIPlayer> createRandomAIPlayer(String name, Long seed){
        CreatePlayerResponse<RandomAIPlayer> response = new CreatePlayerResponse<>();
        if (checkExistenceByName(name, response)) return response;
        RandomAIPlayer newPlayer = new RandomAIPlayer();
        newPlayer.setName(name);
        newPlayer.setSeed(seed);
        response.setCreatedPlayer(playerRepository.save(newPlayer));
        response.setSuccess(true);
        return response;
    }

    /**
     * Creates a minimax AI player. Called from an add AI player form.
     * @param name The name of the player.
     * @param depth The depth of the minimax algorithm
     * @return The created player if the operation was successful, or a list containing error messages.
     */
    public CreatePlayerResponse<MinimaxAIPlayer> createMinmiaxAIPlayer(String name, int depth){
        CreatePlayerResponse<MinimaxAIPlayer> response = new CreatePlayerResponse<>();
        if (checkExistenceByName(name, response)) return response;
        if(depth > 14 || depth < 1){
            response.setSuccess(false);
            response.getMessages().add("Depth must be between 1 and 14");
            return response;
        }
        MinimaxAIPlayer newPlayer = new MinimaxAIPlayer();
        newPlayer.setName(name);
        newPlayer.setDepth(depth);
        response.setCreatedPlayer(playerRepository.save(newPlayer));
        response.setSuccess(true);
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
