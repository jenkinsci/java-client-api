package com.offbytwo.jenkins.client;


public class JenkinsClientException extends RuntimeException {

    private static final long serialVersionUID = -6162604354514189299L;
    private int statusCode = 0;

    public JenkinsClientException(int statusCode, final String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public JenkinsClientException(Exception e) {
        super(e);
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}