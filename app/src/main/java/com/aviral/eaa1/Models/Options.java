package com.aviral.eaa1.Models;

public class Options {

    private int optionImage;
    private String optionDetails;
    private int chances;
    private String optionBalance;

    public Options(int optionImage, String optionDetails, int chances, String optionBalance) {
        this.optionImage = optionImage;
        this.optionDetails = optionDetails;
        this.chances = chances;
        this.optionBalance = optionBalance;
    }

    public int getOptionImage() {
        return optionImage;
    }

    public void setOptionImage(int optionImage) {
        this.optionImage = optionImage;
    }

    public String getOptionDetails() {
        return optionDetails;
    }

    public void setOptionDetails(String optionDetails) {
        this.optionDetails = optionDetails;
    }

    public int getChances() {
        return chances;
    }

    public void setChances(int chances) {
        this.chances = chances;
    }

    public String getOptionBalance() {
        return optionBalance;
    }

    public void setOptionBalance(String optionBalance) {
        this.optionBalance = optionBalance;
    }
}
