package com.aviral.eaa1.Models;

import java.util.Calendar;

public class EarningOptions {

    private int optionImage;

    private String optionTitle;

    private int totalChances;

    private int chancesLeft;

    private double optionEarningAmount;

    private Calendar optionTime;

    private String link;

    private String rewardName;

    public EarningOptions(int optionImage,
                          String optionTitle,
                          int totalChances,
                          int chancesLeft,
                          double optionEarningAmount,
                          Calendar optionTime,
                          String link,
                          String rewardName) {
        this.optionImage = optionImage;
        this.optionTitle = optionTitle;
        this.totalChances = totalChances;
        this.chancesLeft = chancesLeft;
        this.optionEarningAmount = optionEarningAmount;
        this.optionTime = optionTime;
        this.link = link;
        this.rewardName = rewardName;
    }

    public EarningOptions() {
    }

    public int getOptionImage() {
        return optionImage;
    }

    public void setOptionImage(int optionImage) {
        this.optionImage = optionImage;
    }

    public String getOptionTitle() {
        return optionTitle;
    }

    public void setOptionTitle(String optionTitle) {
        this.optionTitle = optionTitle;
    }

    public int getTotalChances() {
        return totalChances;
    }

    public void setTotalChances(int totalChances) {
        this.totalChances = totalChances;
    }

    public int getChancesLeft() {
        return chancesLeft;
    }

    public void setChancesLeft(int chancesLeft) {
        this.chancesLeft = chancesLeft;
    }

    public double getOptionEarningAmount() {
        return optionEarningAmount;
    }

    public void setOptionEarningAmount(double optionEarningAmount) {
        this.optionEarningAmount = optionEarningAmount;
    }

    public Calendar getOptionTime() {
        return optionTime;
    }

    public void setOptionTime(Calendar optionTime) {
        this.optionTime = optionTime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }
}
