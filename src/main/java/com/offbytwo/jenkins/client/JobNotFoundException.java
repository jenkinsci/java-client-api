package com.offbytwo.jenkins.client;

import static java.lang.String.format;


public class JobNotFoundException extends JenkinsClientException {

    private static final long serialVersionUID = -8843373692809103833L;

    public JobNotFoundException(String jobName) {

        super(404, format("Job not found: %s", jobName));
    }
}
