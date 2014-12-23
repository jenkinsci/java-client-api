/*
 * Copyright (c) 2013 Rising Oak LLC.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import com.google.common.base.Function;

import java.util.List;

import static com.google.common.collect.Lists.transform;

public class JobWithDetails extends Job {

    String displayName;
    boolean buildable;
    List<Build> builds;
    Build lastBuild;
    Build lastCompletedBuild;
    Build lastFailedBuild;
    Build lastStableBuild;
    Build lastSuccessfulBuild;
    Build lastUnstableBuild;
    Build lastUnsuccessfulBuild;
    int nextBuildNumber;
    List<Job> downstreamProjects;
    List<Job> upstreamProjects;

    public String getDisplayName() {
        return displayName;
    }

    public boolean isBuildable() {
        return buildable;
    }

    public List<Build> getBuilds() {
        return transform(builds, new Function<Build, Build>() {
            @Override
            public Build apply(Build from) {
                return buildWithClient(from);
            }
        });
    }

    private Build buildWithClient(Build from) {
        // Check for null value first
        if(from == null) {
            return null;
        }

        // Return new value with client being set
        Build ret = new Build(from);
        ret.setClient(client);
        return ret;
    }

    public Build getLastBuild() {
        return buildWithClient(lastBuild);
    }

    public Build getLastCompletedBuild() {
        return buildWithClient(lastCompletedBuild);
    }

    public Build getLastFailedBuild() {
        return buildWithClient(lastFailedBuild);
    }

    public Build getLastStableBuild() {
        return buildWithClient(lastStableBuild);
    }

    public Build getLastSuccessfulBuild() {
        return buildWithClient(lastSuccessfulBuild);
    }

    public Build getLastUnstableBuild() {
        return buildWithClient(lastUnstableBuild);
    }

    public Build getLastUnsuccessfulBuild() {
        return buildWithClient(lastUnsuccessfulBuild);
    }

    public int getNextBuildNumber() {
        return nextBuildNumber;
    }

    public List<Job> getDownstreamProjects() {
        return transform(downstreamProjects, new JobWithClient());
    }

    public List<Job> getUpstreamProjects() {
        return transform(upstreamProjects, new JobWithClient());
    }

    private class JobWithClient implements Function<Job, Job> {
        @Override
        public Job apply(Job job) {
            job.setClient(client);
            return job;
        }
    }
}
