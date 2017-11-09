package com.mastermind.services.game.responses.types;

public class SavedGameRowData {
    private String name;
    private int colorCount;
    private int slotCount;
    private int trials;
    private int maxTrials;
    private String user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getTrials() {
        return trials;
    }

    public void setTrials(int trials) {
        this.trials = trials;
    }

    public int getMaxTrials() {
        return maxTrials;
    }

    public void setMaxTrials(int maxTrials) {
        this.maxTrials = maxTrials;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
