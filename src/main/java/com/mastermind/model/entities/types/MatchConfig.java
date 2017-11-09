package com.mastermind.model.entities.types;

import com.mastermind.model.entities.base.Entity;

import java.util.Objects;

public class MatchConfig extends Entity {
    private int roundCount;
    private int colorCount;
    private int slotCount;
    private int maxTrialCount;
    private boolean allowRepetition;
    private boolean localStartsMakingCode;

    public MatchConfig() {
        roundCount = 4;
        colorCount = 5;
        slotCount = 4;
        maxTrialCount = 10;
        allowRepetition = true;
        localStartsMakingCode = true;
    }

    public MatchConfig(int roundCount, int colorCount, int slotCount, int maxTrialCount, boolean allowRepetition, boolean localStartsMakingCode) {
        this.roundCount = roundCount;
        this.colorCount = colorCount;
        this.slotCount = slotCount;
        this.maxTrialCount = maxTrialCount;
        this.allowRepetition = allowRepetition;
        this.localStartsMakingCode = localStartsMakingCode;
    }

    public MatchConfig(MatchConfig template) {
        roundCount = template.getRoundCount();
        colorCount = template.getColorCount();
        slotCount = template.getSlotCount();
        maxTrialCount = template.getMaxTrialCount();
        allowRepetition = template.isAllowRepetition();
        localStartsMakingCode = template.isLocalStartsMakingCode();
    }

    public int getRoundCount() {
        return roundCount;
    }

    public void setRoundCount(int roundCount) {
        this.roundCount = roundCount;
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

    public int getMaxTrialCount() {
        return maxTrialCount;
    }

    public void setMaxTrialCount(int maxTrialCount) {
        this.maxTrialCount = maxTrialCount;
    }

    private boolean isAllowRepetition() {
        return allowRepetition;
    }

    public void setAllowRepetition(boolean allowRepetition) {
        this.allowRepetition = allowRepetition;
    }

    public boolean isLocalStartsMakingCode() {
        return localStartsMakingCode;
    }

    public void setLocalStartsMakingCode(boolean localStartsMakingCode) {
        this.localStartsMakingCode = localStartsMakingCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchConfig that = (MatchConfig) o;
        return roundCount == that.roundCount &&
                colorCount == that.colorCount &&
                slotCount == that.slotCount &&
                maxTrialCount == that.maxTrialCount &&
                allowRepetition == that.allowRepetition &&
                localStartsMakingCode == that.localStartsMakingCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roundCount, colorCount, slotCount, maxTrialCount, allowRepetition, localStartsMakingCode);
    }
}
