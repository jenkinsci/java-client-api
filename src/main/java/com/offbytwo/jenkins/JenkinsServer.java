/*
 * Copyright (c) 2013 Rising Oak LLC.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.common.net.UrlEscapers;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Computer;
import com.offbytwo.jenkins.model.ComputerSet;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobConfiguration;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.LabelWithDetails;
import com.offbytwo.jenkins.model.MainView;
import com.offbytwo.jenkins.model.MavenJobWithDetails;
import com.offbytwo.jenkins.model.View;

import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.ContentType;
import org.dom4j.DocumentException;

import javax.xml.bind.JAXBException;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * The main starting point for interacting with a Jenkins server.
 */
public class JenkinsServer {

    private final JenkinsHttpClient client;

    /**
     * Create a new Jenkins server reference given only the server address
     *
     * @param serverUri
     *            address of jenkins server (ex. http://localhost:8080/jenkins)
     */
    public JenkinsServer(URI serverUri) {
        this(new JenkinsHttpClient(serverUri));
    }

    /**
     * Create a new Jenkins server reference given the address and credentials
     *
     * @param serverUri
     *            address of jenkins server (ex. http://localhost:8080/jenkins)
     * @param username
     *            username to use when connecting
     * @param passwordOrToken
     *            password (not recommended) or token (recommended)
     */
    public JenkinsServer(URI serverUri, String username, String passwordOrToken) {
        this(new JenkinsHttpClient(serverUri, username, passwordOrToken));
    }

    /**
     * Create a new Jenkins server directly from an HTTP client (ADVANCED)
     *
     * @param client
     *            Specialized client to use.
     */
    public JenkinsServer(JenkinsHttpClient client) {
        this.client = client;
    }

    /**
     * Get the current status of the Jenkins end-point by pinging it.
     *
     * @return true if Jenkins is up and running, false otherwise
     */
    public boolean isRunning() {
        try {
            client.get("/");
            return true;
        } catch (IOException e) {
            return false;
        }
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
     * Get a list of all the defined views on the server (at the summary level)
     *
     * @return list of defined views
     * @throws IOException
     */
    public Map<String, View> getViews() throws IOException {
        List<View> views = client.get("/", MainView.class).getViews();
        return Maps.uniqueIndex(views, new Function<View, String>() {
            @Override
            public String apply(View view) {
                view.setClient(client);
                // return view.getName().toLowerCase();
                return view.getName();
            }
        });
    }

    /**
     * Get a single view object from the server
     *
     * @param name
     *            name of the view in Jenkins
     * @return the view object
     * @throws IOException
     */
    public View getView(String name) throws IOException {
        return client.get("/view/" + encode(name) + "/", View.class);
    }

    /**
     * Get a list of all the defined jobs on the server (at the specified view
     * level)
     *
     * @return list of defined jobs (view level, for details @see Job#details
     * @throws IOException
     */
    public Map<String, Job> getJobs(String view) throws IOException {
        List<Job> jobs = client.get("/view/" + encode(view) + "/", View.class).getJobs();
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
            job.setClient(client);

            return job;
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 404) {
                return null;
            }
            throw e;
        }

    }

    public MavenJobWithDetails getMavenJob(String jobName) throws IOException {
        try {
            MavenJobWithDetails job = client.get("/job/" + encode(jobName), MavenJobWithDetails.class);
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
        client.post_xml("/createItem?name=" + encodeParam(jobName), jobXml);
    }

    public void createJob(String jobName, String jobXml, Boolean crumbFlag) throws IOException {
        client.post_xml("/createItem?name=" + encodeParam(jobName), jobXml, crumbFlag);
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
     * @return list of defined computers (summary level, for details @see
     *         Computer#details
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
     * The ComputerSet class will give informations
     * like {@link ComputerSet#getBusyExecutors()} or
     * the {@link ComputerSet#getTotalExecutors()}.
     * 
     * @return {@link ComputerSet}
     * @throws IOException
     */
    public ComputerSet getComputerSet() throws IOException {
        return client.get("computer/", ComputerSet.class);
    }

    /**
     * Update the xml description of an existing job
     *
     * @return the new job object
     * @throws IOException
     */
    public void updateJob(String jobName, String jobXml) throws IOException {
        this.updateJob(jobName, jobXml, true);
    }

    public void updateJob(String jobName, String jobXml, boolean crumbFlag) throws IOException {
        client.post_xml("/job/" + encode(jobName) + "/config.xml", jobXml, crumbFlag);
    }

    public void addStringParam(String jobName, String name, String description, String defaultValue)
            throws IOException, JAXBException, DocumentException {
        String jobXml = this.getJobXml(jobName);
        JobConfiguration jobConf = new JobConfiguration(jobXml);
        jobXml = jobConf.addStringParam(name, description, defaultValue).asXml();
        this.updateJob(jobName, jobXml);
    }

    /**
     * Sends the Quiet Down (Prepare for shutdown) message
     * 
     * @throws IOException
     */
    public void quietDown() throws IOException {
        try {
            client.get("/quietDown/");
        } catch (org.apache.http.client.ClientProtocolException e) {
            e.printStackTrace();
        }

    }

    /**
     * Cancels the Quiet Down (Prepare for shutdown) message
     * 
     * @throws IOException
     */
    public void cancelQuietDown() throws IOException {
        try {
            client.post("/cancelQuietDown/");
        } catch (org.apache.http.client.ClientProtocolException e) {
            e.printStackTrace();
        }
    }

    /*
     * Delete a job from jenkins
     *
     * @throws IOException
     */
    public void deleteJob(String jobName) throws IOException {
        client.post("/job/" + encode(jobName) + "/doDelete");
    }

    /**
     * Delete a job from Jenkins.
     * 
     * @param jobName
     *            The name of the job to be deleted.
     * @param crumbFlag
     *            The crumFlag.
     * @throws IOException
     *             In case of an failure.
     */
    public void deleteJob(String jobName, boolean crumbFlag) throws IOException {
        client.post("/job/" + encode(jobName) + "/doDelete", crumbFlag);
    }

    /**
     * Runs the provided groovy script on the server and returns the result.
     *
     * This is similar to running groovy scripts using the script console.
     *
     * In the instance where your script causes an exception, the server still
     * returns a 200 status, so detecting errors is very challenging. It is
     * recommended to use heuristics to check your return string for stack
     * traces by detecting strings like "groovy.lang.(something)Exception".
     *
     * @param script
     * @return results
     * @throws IOException
     */
    public String runScript(String script) throws IOException {
        return client.post_text("/scriptText", "script=" + script, ContentType.APPLICATION_FORM_URLENCODED, false);
    }

    private String encode(String pathPart) {
        // jenkins doesn't like the + for space, use %20 instead
        String escape = UrlEscapers.urlPathSegmentEscaper().escape(pathPart);
        return escape;
    }

    private String encodeParam(String pathPart) {
        // jenkins doesn't like the + for space, use %20 instead
        return UrlEscapers.urlFormParameterEscaper().escape(pathPart);
    }
}
