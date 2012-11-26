/*
 * Copyright (c) 2012 Cosmin Stejerean.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

public class MainView extends BaseModel {
    private List<Job> jobs;

    /* default constructor needed for Jackson */
    public MainView() {
        this(Lists.<Job>newArrayList());
    }

    public MainView(List<Job> jobs) {
        this.jobs = jobs;
    }

    public MainView(Job... jobs) {
        this(Arrays.asList(jobs));
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }
}
