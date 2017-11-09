package com.mastermind.model.entities.types;

import com.mastermind.model.entities.base.Entity;
import com.mastermind.model.persistence.RepositoryManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a game between a localPlayer and the AI
 */
public class Match extends Entity {
    private boolean finished = false; //Unfinished games are saved games
    private List<Round> rounds = new ArrayList<>();
    private MatchConfig config;
    private Player localPlayer;
    private Player enemyPlayer;
    private int finishedRoundIndex = -1;
    private Boolean turnActive;

    public Match() {
        config = new MatchConfig();
    }

    public Match(MatchConfig config) {
        this.config = new MatchConfig(config);
    }

    /**
     * @return Whether there are still rounds left to create in the match
     */
    public boolean hasNextRound() {
        return rounds.size() < config.getRoundCount();
    }

    /**
     * Adds a new round to the match
     */
    private void newRound() {
        if (!hasNextRound()) throw new RuntimeException("Match has reached the maximum number of rounds.");
        Round round = new Round(this);
        if (getRoundIndex() % 2 == (config.isLocalStartsMakingCode() ? 0 : 1)) {
            round.setCodemaker(localPlayer);
            round.setCodebreaker(enemyPlayer);
        } else {
            round.setCodemaker(enemyPlayer);
            round.setCodebreaker(localPlayer);
        }
        this.rounds.add(round);
    }

    private void checkFinishRound() {
        if (getCurrentRound().isFinished()) {
            finishedRoundIndex++;
            if (!hasNextRound()) {
                finished = true;
            }
        }
    }

    private int getRoundIndex() {
        return rounds.size() - 1;
    }

    public boolean isInitialized() {
        return getRoundIndex() >= 0;
    }

    /**
     * @return The current uncommitted round or null if there is no such round.
     */
    public Round getCurrentRound() {
        if (!isInitialized()) return null;
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

    public Boolean isTurnActive() {
        return turnActive;
    }

    public void beginTurn() {
        turnActive = true;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    public Player getLocalPlayer() {
        if (localPlayer == null) {
            localPlayer = RepositoryManager.getPlayerRepository()
                    .findByMatch(getId())
                    .orElseThrow(() -> new RuntimeException("Player not found in repository"));
        }
        return localPlayer;
    }

    public void setLocalPlayer(Player localPlayer) {
        this.localPlayer = localPlayer;
    }

    public Player getEnemyPlayer() {
        return enemyPlayer;
    }

    public void setEnemyPlayer(Player enemyPlayer) {
        this.enemyPlayer = enemyPlayer;
    }

    public MatchConfig getConfig() {
        return config;
    }

    // Current round delegates

    /**
     * @return Whether the current round has a next trial
     */
    public boolean hasNextTrial() {
        if (!isInitialized()) return true;
        return getCurrentRound().hasNextTrial();
    }

    /**
     * Creates a new trial for the current round
     */
    public void newTrial() {
        getCurrentRound().newTrial();
    }

    public Optional<Player> getCurrentRoundWinner() {
        return getCurrentRound().getWinner();
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

    public void setCode(Combination code) {
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
//        if(!turnActive) throw new RuntimeException("Tried to set an element without an active turn");
        if (getCurrentRound() == null || getCurrentRound().isFinished()) {
            if (!hasNextRound())
                throw new RuntimeException("Match has reached the maximum number of rounds.");
            newRound();
        }
        if (getCurrentRound().isCurrentTrialCommitted())
            getCurrentRound().newTrial();
        return getCurrentRound().setElement(index, element);
    }

    public void commitTrial() {
        getCurrentRound().commitTrial();
        checkFinishRound();
        // Next player if it is AI
        playAITurn();
    }

    private void playAITurn() {
        if (!isFinished()) {
            if (isCodemakerTurn()) {
                Player codemaker = getCurrentRound().getCodemaker();
                if (codemaker instanceof AIPlayer) {
                    ((AIPlayer) codemaker).playAsCodemaker(this);
                }
            }
            if (isCodebrekerTurn()) {
                Player codebreaker = getCurrentRound().getCodebreaker();
                if (codebreaker instanceof AIPlayer) {
                    ((AIPlayer) codebreaker).playAsCodebreaker(this);
                }
            }
        }
    }

    public TrialEvaluation getLastCommittedTrialEvaluation() {
        if (getCurrentRound().getLastCommittedTrial() == null) return null;
        return getCurrentRound().getLastCommittedTrial().getTrialEvaluation();
    }

    public boolean isCurrentRoundFinished() {
        return getCurrentRound().isFinished();
    }

    private boolean isActivePlayerCodemaker() {
        if (!isInitialized()) {
            return getConfig().isLocalStartsMakingCode();
        }
        if (getCurrentRound().isFinished()) return true;
        return getCurrentRound().isActivePlayerCodemaker();
    }

    public boolean isCodemakerTurn() {
        if (!isInitialized()) {
            return getConfig().isLocalStartsMakingCode();
        }
        if (isFinished()) return false;
        return isActivePlayerCodemaker();
    }

    public boolean isCodebrekerTurn() {
        if (!isInitialized()) {
            return !getConfig().isLocalStartsMakingCode();
        }
        if (isFinished()) return false;
        if (getCurrentRound().isFinished()) return false;
        return !isActivePlayerCodemaker();
    }

}
