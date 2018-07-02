/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpConnection;

public class MainView extends BaseModel {

    private List<Job> jobs;
    private List<View> views;
    private JenkinsServer jenkinsServer;

    /* default constructor needed for Jackson */
    public MainView() {
        this(Lists.<Job>newArrayList(), Lists.<View>newArrayList());
    }

    public MainView(List<Job> jobs, List<View> views) {
        this.jobs = jobs;
        this.views = views;
    }

    public MainView(Job... jobs) {
        this(Arrays.asList(jobs), Lists.<View>newArrayList());
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public List<View> getViews() {
        return views;
    }

    public void setViews(List<View> views) {
        this.views = views;
    }

    @Override
    public void setClient(JenkinsHttpConnection client) {
        super.setClient(client);
        getJobs().forEach(j -> j.setClient(client));
        getViews().forEach(j -> j.setClient(client));
    }

    public MainView setJenkinsServer(JenkinsServer jenkinsServer) {
        this.jenkinsServer = jenkinsServer;
        return this;
    }

    public JenkinsServer getJenkinsServer() {
        return jenkinsServer;
    }

}
