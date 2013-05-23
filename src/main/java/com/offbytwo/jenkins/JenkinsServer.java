/*
 * Copyright (c) 2012 Cosmin Stejerean.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.MainView;

/**
 * The main starting point for interacting with a Jenkins server.
 */
public class JenkinsServer {
    private final JenkinsHttpClient client;


    /**
     * Create a new Jenkins server reference given only the server address
     *
     * @param serverUri address of jenkins server (ex. http://localhost:8080/jenkins)
     */
    public JenkinsServer(URI serverUri) {
        this(new JenkinsHttpClient(serverUri));
    }

    /**
     * Create a new Jenkins server reference given the address and credentials
     *
     * @param serverUri address of jenkins server (ex. http://localhost:8080/jenkins)
     * @param username username to use when connecting
     * @param passwordOrToken password (not recommended) or token (recommended)
     */
    public JenkinsServer(URI serverUri, String username, String passwordOrToken) {
        this(new JenkinsHttpClient(serverUri, username, passwordOrToken));
    }

    /**
     * Create a new Jenkins server directly from an HTTP client (ADVANCED)
     *
     * @param client Specialized client to use.
     */
    public JenkinsServer(JenkinsHttpClient client) {
        this.client = client;
    }

    /**
     * Get a list of all the defined jobs on the server (at the summary level)
     *
     * @return list of defined jobs (summary level, for details @see Job#details
     * @throws IOException
     */
    public Map<String, Job> getJobs() throws IOException {
        List<Job> jobs = client.get("/", MainView.class).getJobs();
        return Maps.uniqueIndex(jobs, new Function<Job, String>() {
            @Override
            public String apply(Job job) {
                job.setClient(client);
                return job.getName().toLowerCase();
            }
        });
    }

    /**
     * Create a job on the server using the provided xml
     *
     * @return the new job object
     * @throws IOException
     */
    public void createJob(String jobName, String jobXml) throws IOException {
        client.post_xml("/createItem?name=" + jobName, jobXml);
    }

    /**
     * Get the xml description of an existing job
     *
     * @return the new job object
     * @throws IOException
     */
    public String getJobXml(String jobName) throws IOException {
        return client.get("/job/" + jobName + "/config.xml");
    }

    /**
     * Update the xml description of an existing job
     *
     * @return the new job object
     * @throws IOException
     */
    public void updateJob(String jobName, String jobXml) throws IOException {
        client.post_xml("/job/" + jobName + "/config.xml", jobXml);
    }
}
