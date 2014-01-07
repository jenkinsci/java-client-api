/*
 * Copyright (c) 2013 Rising Oak LLC.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.*;
import org.apache.http.client.HttpResponseException;

/**
 * The main starting point for interacting with a Jenkins server.
 */
public class JenkinsServer {
    private JenkinsHttpClient client;


    /**
     * Create a new Jenkins server reference given only the server address
     */
    private JenkinsServer() {

    }

    /**
     * Create a new Jenkins server reference given only the server address
     *
     * @param client address of jenkins server (ex. http://localhost:8080/jenkins)
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
     * Get a single Job from the server.
     *
     * @return A single Job, null if not present
     * @throws IOException
     */
    public JobWithDetails getJob(String jobName) throws IOException {
        try {
            JobWithDetails job = client.get("/job/" + encode(jobName), JobWithDetails.class);
            System.out.println("/job/" + encode(jobName));
            job.setClient(client);

            return job;
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 404) {
                return null;
            }
            throw e;
        }

    }

    /**
     * Create a job on the server using the provided xml
     *
     * @return the new job object
     * @throws IOException
     */
    public void createJob(String jobName, String jobXml) throws IOException {
        client.post_xml("/createItem?name=" + encode(jobName), jobXml);
    }

    /**
     * Get the xml description of an existing job
     *
     * @return the new job object
     * @throws IOException
     */
    public String getJobXml(String jobName) throws IOException {
        return client.get("/job/" + encode(jobName) + "/config.xml");
    }

    /**
     * Get the description of an existing Label
     *
     * @return label object
     * @throws IOException
     */
    public LabelWithDetails getLabel(String labelName) throws IOException {
        return client.get("/label/" + encode(labelName), LabelWithDetails.class);
    }


    /**
     * Get a list of all the computers on the server (at the summary level)
     *
     * @return list of defined computers (summary level, for details @see Computer#details
     * @throws IOException
     */
    public Map<String, Computer> getComputers() throws IOException {
        List<Computer> computers = client.get("computer/", Computer.class).getComputers();
        return Maps.uniqueIndex(computers, new Function<Computer, String>() {
            @Override
            public String apply(Computer computer) {
                computer.setClient(client);
                return computer.getDisplayName().toLowerCase();
            }
        });
    }

    /**
     * Update the xml description of an existing job
     *
     * @return the new job object
     * @throws IOException
     */
    public void updateJob(String jobName, String jobXml) throws IOException {
        client.post_xml("/job/" + encode(jobName) + "/config.xml", jobXml);
    }

    private String encode(String pathPart) {
        // jenkins doesn't like the + for space, use %20 instead
        return URLEncoder.encode(pathPart).replaceAll("\\+", "%20");
    }
}
