package com.mastermind.services.game;

import com.mastermind.model.entities.types.*;
import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.MatchRepository;
import com.mastermind.model.persistence.repositories.PlayerRepository;
import com.mastermind.services.Service;
import com.mastermind.services.ServiceManager;
import com.mastermind.services.ServiceState;
import com.mastermind.services.game.responses.ListSavedGamesResponse;
import com.mastermind.services.game.responses.exceptions.MatchNotYoursException;
import com.mastermind.services.game.responses.exceptions.NoActiveMatchException;
import com.mastermind.services.game.responses.exceptions.UserNotLoggedInException;
import com.mastermind.services.game.responses.types.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service that takes care of actions performed from the game window
 */
public class GameService implements Service {
    private MatchRepository matchRepository = RepositoryManager.getMatchRepository();
    private PlayerRepository playerRepository = RepositoryManager.getPlayerRepository();

    private List<Player> getChooseEnemyPlayerList() {
        return playerRepository.findAllByAITrue();
    }

    public List<AIPlayerRowData> listEnemyPlayers() {
        return getChooseEnemyPlayerList().stream()
                .map(player -> {
                    AIPlayerRowData rowData = new AIPlayerRowData();
                    rowData.setName(player.getName());
                    rowData.setType(player.getClass().getSimpleName());
                    return rowData;
                })
                .collect(Collectors.toList());
    }

    /**
     * Starts a new game with the current settings. The game is not saved until some other action is performed.
     * Called from a new game button. Should require confirmation.
     */
    public UserGameState newGame(int enemyPlayerIndex) {
        requireLogin();
        Match activeMatch = getActiveMatch();
        Match newMatch = new Match(getState().getLoggedInPlayer(),
                getChooseEnemyPlayerList().get(enemyPlayerIndex),
                new MatchConfig(getState().getEnviromentConfig()));
        getState().setActiveMatch(newMatch);
        return getUserGameState();
    }

    private void requireLogin() {
        if (getState().getLoggedInPlayer() == null) throw new UserNotLoggedInException();
    }

    private ServiceState getState() {
        return ServiceManager.getState();
    }


    /**
     * Called from a repeat game button
     * Restarts game with the same settings and combination
     */
    public UserGameState repeatGame() {
        requireActiveMatch();
        Match oldMatch = getState().getActiveMatch();
        Match newMatch = new Match(oldMatch.getLocalPlayer(), oldMatch.getEnemyPlayer(), oldMatch.getConfig());
        getState().setActiveMatch(newMatch);
        return getUserGameState();
    }

    private void requireActiveMatch() {
        requireLogin();
        Match activeMatch = getState().getActiveMatch();
        if (activeMatch == null) throw new NoActiveMatchException();
        if (!activeMatch.getLocalPlayer().equals(getState().getLoggedInPlayer()))
            throw new MatchNotYoursException(activeMatch, getState().getLoggedInPlayer());
    }

    /**
     * Places a color into the current uncommitted trial.
     * Called on drag and drop into the combination box.
     *
     * @param position The target position within the combination
     * @param colorId  The identifier of the color to place
     */
    public UserGameState placeColor(int position, int colorId) {
        requireActiveMatch();
        getActiveMatch().setElement(position, colorId);
        getState().saveActiveMatch();
        return getUserGameState();
    }

    /**
     * Copies the combination from the previous trial into the current one
     */
    public UserGameState duplicatePreviousTrial() {
        requireActiveMatch();
        Trial lastCommittedTrial = getActiveMatch().getCurrentRound().getLastCommittedTrial();
        Combination combination = lastCommittedTrial.getCombination();
        for (int i = 0; i < combination.getSize(); i++) {
            getActiveMatch().getCurrentRound().setElement(i, combination.getElements().get(i));
        }
        getState().saveActiveMatch();
        return getUserGameState();
    }

    /**
     * The action of committing the current trial
     *
     * @return The evaluations of the committed trial and whether the game has finished or not
     */
    public UserGameState commitMove() {
        requireActiveMatch();
        getActiveMatch().commitMove();
        getState().saveActiveMatch();
        return getUserGameState();
    }

    private Match getActiveMatch() {
        return getState().getActiveMatch();
    }

    /**
     * List the saved games. To be displayed on a table to choose and load.
     *
     * @return The list of saved games.
     */
    public ListSavedGamesResponse listSavedGames() {
        requireLogin();
        ListSavedGamesResponse response = new ListSavedGamesResponse();
        // Access repositories and fill response
        response.setSavedGameRows(getSavedGamesList().stream()
                .map(match -> {
                    SavedGameRowData rowData = new SavedGameRowData();
                    rowData.setColorCount(match.getConfig().getColorCount());
                    rowData.setMaxTrials(match.getConfig().getMaxTrialCount());
                    rowData.setName(MessageFormat.format("{0} vs {1}", match.getLocalPlayer().getName(), match.getEnemyPlayer().getName()));
                    rowData.setSlotCount(match.getConfig().getSlotCount());
                    rowData.setTrials(match.getConfig().getMaxTrialCount());
                    rowData.setUser(match.getLocalPlayer().getName());
                    return rowData;
                }).collect(Collectors.toList()));
        return response;
    }

    private List<Match> getSavedGamesList() {
        requireLogin();
        return matchRepository.findByPlayerAndFinishedFalse(getState().getLoggedInPlayer().getId());
    }

    /**
     * Loads a game from the list of saved games, identified by its position on the list.
     *
     * @param index The position of the game in the list of saved games.
     * @return The data needed to load the game and continue playing
     */
    public UserGameState loadSavedGame(int index) {
        Match match = getSavedGamesList().get(index);
        getState().setActiveMatch(match);
        return getUserGameState();
    }

    /**
     * Removes a game from the list of saved games, identified by its position on the list
     *
     * @param index The position of the game in the list of saved games.
     */
    public void removeSavedGame(int index) {
        matchRepository.delete(getSavedGamesList().get(index));
    }

    public UserGameState getUserGameState() {
        UserGameState state = new UserGameState();
        Match match = getActiveMatch();


        // Config
        state.setColorCount(getTargetConfig().getColorCount());
        state.setSlotCount(getTargetConfig().getSlotCount());
        state.setMaxTrialCount(getTargetConfig().getMaxTrialCount());
        state.setTotalRoundCount(getTargetConfig().getRoundCount());
        state.setAllowRepetition(getTargetConfig().isAllowRepetition());
        state.setLocalStartsMakingCode(getTargetConfig().isLocalStartsMakingCode());
        if (match == null) {
            state.setMessage("Choose an enemy and start a game!");
            state.setMatchStatus(UserGameState.MatchStatus.NOT_CREATED);
            return state;
        }
        Round round = match.getCurrentRound();
        state.setLocalPlayerName(match.getLocalPlayer().getName());
        state.setEnemyPlayerName(match.getEnemyPlayer().getName());
        state.setLocalPlayerRole(match.getConfig().isLocalStartsMakingCode() ? UserGameState.Role.CODEMAKER : UserGameState.Role.CODEBREAKER);
        state.setCurrentRound(match.getRounds().size() - 1);
        boolean middleFinishLine = false;
        if(match.getLastFinishedRound() != null
                && match.getCurrentRound() != null && match.getCurrentRound().getTrials().size() == 0)
            middleFinishLine = true;
        state.setCurrentRoundFinished(middleFinishLine || match.isCurrentRoundFinished());
        if (!match.isModified()) {
            state.setMatchStatus(UserGameState.MatchStatus.NOT_STARTED);
            state.setMessage("Start by placing your pegs!");
            return state;
        }
        if(match.getLastFinishedRound() != null && match.getLastFinishedRoundWinner().isPresent()){
            state.setLastFinishedRoundWinner(match.getLastFinishedRoundWinner().get().getName());
        }
        state.setMatchStatus(UserGameState.MatchStatus.IN_PROGRESS);
        UserGameState.Role localPlayerRole = round.getCodemaker().getId().equals(match.getLocalPlayer().getId()) ?
                UserGameState.Role.CODEMAKER : UserGameState.Role.CODEBREAKER;
        state.setLocalPlayerRole(localPlayerRole);
        // Code
        if (round.getCode() != null) {
            CombinationData code = new CombinationData();
            code.setElements(new ArrayList<>(round.getCode().getElements()));
            state.setCode(code);
        }
        state.setMessage(localPlayerRole == UserGameState.Role.CODEMAKER ? "Set your code!" : "Break the code!");
        state.setTrials(round.getTrials().stream().map(trial -> {
            TrialData data = new TrialData();

            CombinationData combinationData = new CombinationData();
            combinationData.setElements(new ArrayList<>(trial.getCombination().getElements()));
            data.setCombinationData(combinationData);

            TrialEvaluation trialEvaluation = trial.getTrialEvaluation();
            if (trialEvaluation != null) {
                EvaluationData evaluationData = new EvaluationData();
                evaluationData.setCorrectPlaceAndColorCount(trialEvaluation.getCorrectPlaceAndColorCount());
                evaluationData.setCorrectColorCount(trialEvaluation.getCorrectColorCount());
                data.setEvaluationData(evaluationData);
            }

            return data;
        }).collect(Collectors.toList()));
        if (match.isFinished()) {
            state.setMatchStatus(UserGameState.MatchStatus.FINISHED);
            state.setLocalWins(match.getWinner().equals(match.getLocalPlayer()));
            state.setLocalPlayerEloIncrement(match.getLocalPlayerEloIncrement());
        }
        return state;
    }
    private MatchConfig getTargetConfig() {
        return getState().getEnviromentConfig();
    }

    /**
     * @param roundCount The number of rounds that the match will have
     */
    public void setRoundCount(int roundCount) {
        getTargetConfig().setRoundCount(roundCount);
    }
    /**
     * @param colorCount The number of colors available to make and break the code
     */
    public void setColorCount(int colorCount) {
        getTargetConfig().setColorCount(colorCount);
    }

    /**
     * @param slotCount Length of the code (and of the trials to break it)
     */
    public void setSlotCount(int slotCount) {
        getTargetConfig().setSlotCount(slotCount);
    }

    /**
     * @param allowRepetition Whether element repetition in the same combination will be allowed
     */
    public void setAllowRepetition(boolean allowRepetition) {
        getTargetConfig().setAllowRepetition(allowRepetition);
    }

    /**
     * @param localStartsMakingCode Whether the local or the enemy should begin making the code
     */
    public void setLocalStartsMakingCode(boolean localStartsMakingCode) {
        getTargetConfig().setLocalStartsMakingCode(localStartsMakingCode);
    }

    /**
     * @param maxTrialCount The number of trials the player has to break the code before the codemaker wins
     */
    public void setMaxTrialCount(int maxTrialCount){
        getTargetConfig().setMaxTrialCount(maxTrialCount);
    }
}
