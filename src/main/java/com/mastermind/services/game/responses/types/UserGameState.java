package com.mastermind.services.game.responses.types;

import java.util.List;

public class UserGameState {
    public enum Role {CODEMAKER, CODEBREAKER}

    public enum MatchStatus {NOT_STARTED, IN_PROGRESS, FINISHED}

    private int currentRound;
    private int totalRoundCount;
    private CombinationData code;
    private String message;
    private List<TrialData> trials;
    private int maxTrialCount;
    private int colorCount;
    private int slotCount;
    private boolean allowRepetition;
    private Role localPlayerRole;
    private String localPlayerName;
    private String enemyPlayerName; // AI
    private Boolean localWins;
    private MatchStatus matchStatus;

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public int getTotalRoundCount() {
        return totalRoundCount;
    }

    public void setTotalRoundCount(int totalRoundCount) {
        this.totalRoundCount = totalRoundCount;
    }

    /**
     * @return The secret code for the current game.
     */
    public CombinationData getCode() {
        return code;
    }

    public void setCode(CombinationData code) {
        this.code = code;
    }

    /**
     * @return The message to be shown to the player
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * The list of trials for the current game.
     * The first n-1 are committed and the last is not if there is available space
     *
     * @return
     */
    public List<TrialData> getTrials() {
        return trials;
    }

    public void setTrials(List<TrialData> trials) {
        this.trials = trials;
    }

    public int getMaxTrialCount() {
        return maxTrialCount;
    }

    public void setMaxTrialCount(int maxTrialCount) {
        this.maxTrialCount = maxTrialCount;
    }

    public int getColorCount() {
        return colorCount;
    }

    public void setColorCount(int colorCount) {
        this.colorCount = colorCount;
    }

    public int getSlotCount() {
        return slotCount;
    }

    public void setSlotCount(int slotCount) {
        this.slotCount = slotCount;
    }

    public String getLocalPlayerName() {
        return localPlayerName;
    }

    public void setLocalPlayerName(String localPlayerName) {
        this.localPlayerName = localPlayerName;
    }

    public Role getLocalPlayerRole() {
        return localPlayerRole;
    }

    public void setLocalPlayerRole(Role localPlayerRole) {
        this.localPlayerRole = localPlayerRole;
    }

    public String getEnemyPlayerName() {
        return enemyPlayerName;
    }

    public void setEnemyPlayerName(String enemyPlayerName) {
        this.enemyPlayerName = enemyPlayerName;
    }

    public Boolean getLocalWins() {
        return localWins;
    }

    public void setLocalWins(Boolean localWins) {
        this.localWins = localWins;
    }

    public MatchStatus getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(MatchStatus matchStatus) {
        this.matchStatus = matchStatus;
    }

    public boolean isAllowRepetition() {
        return allowRepetition;
    }

    public void setAllowRepetition(boolean allowRepetition) {
        this.allowRepetition = allowRepetition;
    }
}
