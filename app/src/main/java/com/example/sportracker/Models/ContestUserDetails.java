package com.example.sportracker.Models;

// TODO: Delete this - use UserStatistics
public class ContestUserDetails {
    private int winAmount;
    private int lossAmount;

    public ContestUserDetails(int winAmount, int lossAmount) {
        this.winAmount = winAmount;
        this.lossAmount = lossAmount;
    }

    public int getWinAmount() {
        return winAmount;
    }

    public void setWinAmount(int winAmount) {
        this.winAmount = winAmount;
    }

    public int getLossAmount() {
        return lossAmount;
    }

    public void setLossAmount(int lossAmount) {
        this.lossAmount = lossAmount;
    }

    public void incrementWinAmount() {
        this.winAmount++;
    }

    public void incrementLossAmount() {
        this.lossAmount++;
    }

    public float getWinLossRatio() {
        if (this.lossAmount == 0) {
            return this.winAmount == 0 ? 0 : 1;
        } else {
            return ((float) this.winAmount) / this.lossAmount;
        }
    }
}
