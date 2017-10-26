package com.mastermind.services.game.responses;

import com.mastermind.services.game.responses.types.ConfigData;
import com.mastermind.services.game.responses.types.TrialData;

import java.util.List;

public class LoadGameResponse {
    private boolean success;
    private List<TrialData> trials;
    private ConfigData config;

    // TODO Describe board without referencing entity classes
    // (use additional responses)
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<TrialData> getTrials() {
        return trials;
    }

    public void setTrials(List<TrialData> trials) {
        this.trials = trials;
    }

    public ConfigData getConfig() {
        return config;
    }

    public void setConfig(ConfigData config) {
        this.config = config;
    }
}
