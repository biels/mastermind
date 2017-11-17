package com.mastermind.model.entities.types;

import com.mastermind.logic.ComponentManager;
import com.mastermind.logic.EloExchangerComponent;
import com.mastermind.logic.types.Pair;
import com.mastermind.model.entities.base.Entity;
import com.mastermind.model.persistence.RepositoryManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Represents a game between a localPlayer and the AI
 */
public class Match extends Entity {
    private boolean finished = false; //Unfinished games are saved games
    private List<Round> rounds = new ArrayList<>();
    private MatchConfig config;
    private Player localPlayer;
    private Player enemyPlayer;
    private Double totalEloExchange;
    private int finishedRoundIndex = -1;
    private Player winner = null;
    private boolean modified = false;
    // Add playAITurnIfNeeded to begin games when AI goes first

    public Match(Player localPlayer, Player enemyPlayer) {
        this(localPlayer, enemyPlayer, new MatchConfig());
    }

    public Match(Player localPlayer, Player enemyPlayer, MatchConfig config) {
        this.localPlayer = localPlayer;
        this.enemyPlayer = enemyPlayer;
        this.config = new MatchConfig(config);
        newRound();
    }

    /**
     * @return Whether there are still rounds left to create in the match
     */
    private boolean hasNextRound() {
        return rounds.size() < config.getRoundCount();
    }

    /**
     * Adds a new round to the match
     */
    private void newRound() {
        if (!hasNextRound()) throw new RuntimeException("Match has reached the maximum number of rounds.");
        Round round;
        if ((rounds.size()) % 2 == (config.isLocalStartsMakingCode() ? 0 : 1)) {
            round = new Round(this, localPlayer, enemyPlayer);
        } else {
            round = new Round(this, enemyPlayer, localPlayer);
        }
        this.rounds.add(round);
    }

    private void checkFinishRound() {
        if (getCurrentRound().isFinished()) {
            finishedRoundIndex++;
            if (!hasNextRound()) {
                finished = true;
                determineWinner();
                exchangeElo();
            }
        }
    }
    private void determineWinner(){
        Predicate<Round> localPlayerFilter = round -> round.getWinner().get().equals(localPlayer);
        Predicate<Round> enemyPlayerFilter = round -> round.getWinner().get().equals(enemyPlayer);
        long localPlayerWins = rounds.stream()
                .filter(localPlayerFilter)
                .count();
        long enemyPlayerWins = rounds.stream()
                .filter(enemyPlayerFilter)
                .count();
        if(localPlayerWins == enemyPlayerWins){
            int localPlayerWinScore = rounds.stream()
                    .filter(localPlayerFilter)
                    .mapToInt(Round::getWinScore)
                    .sum();
            int enemyPlayerWinScore = rounds.stream()
                    .filter(enemyPlayerFilter)
                    .mapToInt(Round::getWinScore)
                    .sum();
            if (localPlayerWinScore <= enemyPlayerWinScore){
                winner = localPlayer;
            }else{
                winner = enemyPlayer;
            }
            return;
        }
        winner = localPlayerWins < enemyPlayerWins ? localPlayer : enemyPlayer;

    }
    private void exchangeElo(){
        EloExchangerComponent eloExchanger = ComponentManager.getEloExchangerComponent();
        Pair<Double, Double> exchangeAmount = eloExchanger.calculateExchangeAmount
                (getWinner().getElo(), getLoser().getElo(), 1, getK(), false);
        totalEloExchange = exchangeAmount.getFirst() + exchangeAmount.getSecond();
        getWinner().incrementElo(exchangeAmount.getFirst());
        getLoser().incrementElo(exchangeAmount.getSecond());
    }

    private double getK() {
        return 32D;
    }

    public Player getWinner() {
        return winner;
    }
    public Player getLoser(){
        return (localPlayer == winner ? enemyPlayer : localPlayer);
    }
    private int getRoundIndex() {
        return rounds.size() - 1;
    }

    /**
     * @return The current uncommitted round or null if there is no such round.
     */
    public Round getCurrentRound() {
        return rounds.get(getRoundIndex());
    }

    /**
     * @return The last finished round or null if there is no such round
     */
    public Round getLastFinishedRound() {
        if (finishedRoundIndex == -1) return null;
        return rounds.get(finishedRoundIndex);
    }

    public boolean isFinished() {
        return finished;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public Player getLocalPlayer() {
        if (localPlayer == null) {
            localPlayer = RepositoryManager.getPlayerRepository()
                    .findByMatch(getId())
                    .orElseThrow(() -> new RuntimeException("Player not found in repository"));
        }
        return localPlayer;
    }

    private void setLocalPlayer(Player localPlayer) {
        this.localPlayer = localPlayer;
    }

    public Player getEnemyPlayer() {
        return enemyPlayer;
    }

    private void setEnemyPlayer(Player enemyPlayer) {
        this.enemyPlayer = enemyPlayer;
    }

    public MatchConfig getConfig() {
        return config;
    }

    public boolean isModified() {
        return modified;
    }
// Current round delegates

    /**
     * @return Whether the current round has a next trial
     */
    private boolean hasNextTrial() {
        return getCurrentRound().hasNextTrial();
    }

    public Optional<Player> getLastFinishedRoundWinner() {
        if(getLastFinishedRound() == null) return Optional.empty();
        return getLastFinishedRound().getWinner();
    }

    /**
     * Gets the current trial for the current round
     */
    public Trial getCurrentTrial() {
        return getCurrentRound().getCurrentTrial();
    }

    public Player getCurrentRoundCodemaker() {
        return getCurrentRound().getCodemaker();
    }

    public Player getCurrentRoundCodebreaker() {
        return getCurrentRound().getCodebreaker();
    }

    public Combination getCode() {
        return getCurrentRound().getCode();
    }

    private void setCode(Combination code) {
        if (getCurrentRound() == null) {
            newRound();
        }
        getCurrentRound().setCode(code);
    }

    /**
     * Sets an element on the current round's uncommitted trial's combination.
     * If there is no uncommitted trial, it creates a new one
     *
     * @throws RuntimeException if a new trial needs to be created, but there are no trials left
     */
    public Integer setElement(int index, Integer element) {
        modified = true;
        if (getCurrentRound() == null || getCurrentRound().isFinished()) {
            if (!hasNextRound())
                throw new RuntimeException("Match has reached the maximum number of rounds.");
            newRound();
        }
        return getCurrentRound().setElement(index, element);
    }

    public void commitMove() {
        boolean wasCodemakerTurn = getCurrentRound().isActivePlayerCodemaker();
        getCurrentRound().commitMove();
        checkFinishRound();
        if(isFinished())
            return;
        // Next player if it is AI
        boolean wasRoundFinished = getCurrentRound().isFinished();
        if(getCurrentRound().isFinished())
            newRound();
    }
    public TrialEvaluation getLastCommittedTrialEvaluation() {
        if (getCurrentRound().getLastCommittedTrial() == null) return null;
        return getCurrentRound().getLastCommittedTrial().getTrialEvaluation();
    }
    public boolean isCurrentRoundFinished() {
        if(getCurrentRound() == null) throw new RuntimeException("There is no current round");
        return getCurrentRound().isFinished();
    }

    private boolean isActivePlayerCodemaker() {
        return getCurrentRound().isActivePlayerCodemaker();
    }

    public boolean isCodemakerTurn() {
        return isActivePlayerCodemaker();
    }

    public boolean isCodebreakerTurn() {
        return !isActivePlayerCodemaker();
    }

}
