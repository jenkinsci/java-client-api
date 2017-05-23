package com.offbytwo.jenkins.model;

public class JacocoCoverageResult {

    private int covered;
    private int missed;
    private int percentage;
    private int percentageFloat;
    private int total;

    public int getCovered() {
        return covered;
    }
    public void setCovered(int covered) {
        this.covered = covered;
    }
    public int getMissed() {
        return missed;
    }
    public void setMissed(int missed) {
        this.missed = missed;
    }
    public int getPercentage() {
        return percentage;
    }
    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
    public int getPercentageFloat() {
        return percentageFloat;
    }
    public void setPercentageFloat(int percentageFloat) {
        this.percentageFloat = percentageFloat;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }



}