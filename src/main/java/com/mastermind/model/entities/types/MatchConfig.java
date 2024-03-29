package com.mastermind.model.entities.types;

import com.mastermind.model.entities.base.Entity;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Objects;

/**
 * Configuration for the match. Ensures sensible values will be provided. It will silently update any unexpected value
 */
public class MatchConfig extends Entity {
    private int roundCount;
    private int colorCount;
    private int slotCount;
    private int maxTrialCount;
    private boolean allowRepetition;
    private boolean localStartsMakingCode;


    public MatchConfig(String s) {
        String s3 = ":";
        String[] split = s.split(s3);
        this.roundCount = Integer.parseInt(split[0]);
        this.colorCount = Integer.parseInt(split[1]);
        this.slotCount = Integer.parseInt(split[2]);
        this.maxTrialCount = Integer.parseInt(split[3]);
        this.allowRepetition = Boolean.parseBoolean(split[4]);
        this.localStartsMakingCode = Boolean.parseBoolean(split[5]);
    }


    public MatchConfig() {
        roundCount = 4;
        colorCount = 6;
        slotCount = 4;
        maxTrialCount = 10;
        allowRepetition = true;
        localStartsMakingCode = false;
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
        if(roundCount < 1) roundCount = 1;
        return roundCount;
    }

    public void setRoundCount(int roundCount) {
        this.roundCount = roundCount;
    }

    public int getColorCount() {
        if((colorCount < slotCount) && !allowRepetition) colorCount = slotCount;
        return colorCount;
    }

    public void setColorCount(int colorCount) {
        if(colorCount < 2) colorCount = 2;
        this.colorCount = colorCount;
    }

    public int getSlotCount() {
        if(slotCount < 2) slotCount = 2;
        return slotCount;
    }

    public void setSlotCount(int slotCount) {
        this.slotCount = slotCount;
    }

    public int getMaxTrialCount() {
        if(maxTrialCount < 1) maxTrialCount = 1;
        return maxTrialCount;
    }

    public void setMaxTrialCount(int maxTrialCount) {
        this.maxTrialCount = maxTrialCount;
    }

    public boolean isAllowRepetition() {
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
    public String serialize(){
        String s3 = ":";
        return roundCount + s3 + // 0
                colorCount + s3 + // 1
                slotCount + s3 + // 2
                maxTrialCount + s3 + // 3
                allowRepetition + s3 + // 4
                localStartsMakingCode; // 5
    }
}
