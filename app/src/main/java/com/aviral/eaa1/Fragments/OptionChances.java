package com.aviral.eaa1.Fragments;

public class OptionChances {

    private int dailyBonusChances;

    private int collectRewardsChances;

    private int watchVideosChances;

    private int goldPointsChances;

    public OptionChances(int dailyBonusChances,
                         int collectRewardsChances,
                         int watchVideosChances,
                         int goldPointsChances) {
        this.dailyBonusChances = dailyBonusChances;
        this.collectRewardsChances = collectRewardsChances;
        this.watchVideosChances = watchVideosChances;
        this.goldPointsChances = goldPointsChances;
    }

    public int getDailyBonusChances() {
        return dailyBonusChances;
    }

    public void setDailyBonusChances(int dailyBonusChances) {
        this.dailyBonusChances = dailyBonusChances;
    }

    public int getCollectRewardsChances() {
        return collectRewardsChances;
    }

    public void setCollectRewardsChances(int collectRewardsChances) {
        this.collectRewardsChances = collectRewardsChances;
    }

    public int getWatchVideosChances() {
        return watchVideosChances;
    }

    public void setWatchVideosChances(int watchVideosChances) {
        this.watchVideosChances = watchVideosChances;
    }

    public int getGoldPointsChances() {
        return goldPointsChances;
    }

    public void setGoldPointsChances(int goldPointsChances) {
        this.goldPointsChances = goldPointsChances;
    }
}
