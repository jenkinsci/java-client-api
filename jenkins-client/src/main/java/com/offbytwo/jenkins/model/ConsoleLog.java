package com.offbytwo.jenkins.model;

/**
 * Represents build console log
 */
public class ConsoleLog {

    private String consoleLog;
    private Boolean hasMoreData;
    private Integer currentBufferSize;

    public ConsoleLog(String consoleLog, Boolean hasMoreData, Integer currentBufferSize) {
        this.consoleLog = consoleLog;
        this.hasMoreData = hasMoreData;
        this.currentBufferSize = currentBufferSize;
    }

    public String getConsoleLog() {
        return consoleLog;
    }

    public void setConsoleLog(String consoleLog) {
        this.consoleLog = consoleLog;
    }

    public Boolean getHasMoreData() {
        return hasMoreData;
    }

    public void setHasMoreData(Boolean hasMoreData) {
        this.hasMoreData = hasMoreData;
    }

    public Integer getCurrentBufferSize() {
        return currentBufferSize;
    }

    public void setCurrentBufferSize(Integer currentBufferSize) {
        this.currentBufferSize = currentBufferSize;
    }
}
