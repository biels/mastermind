package com.mastermind.services.game.responses.types;

public class SavedGameRowData {
    private String name;
    private String colorCount;
    private String slotCount;
    private String trials;
    private String maxTrials;
    private String user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColorCount() {
        return colorCount;
    }

    public void setColorCount(String colorCount) {
        this.colorCount = colorCount;
    }

    public String getSlotCount() {
        return slotCount;
    }

    public void setSlotCount(String slotCount) {
        this.slotCount = slotCount;
    }

    public String getTrials() {
        return trials;
    }

    public void setTrials(String trials) {
        this.trials = trials;
    }

    public String getMaxTrials() {
        return maxTrials;
    }

    public void setMaxTrials(String maxTrials) {
        this.maxTrials = maxTrials;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
