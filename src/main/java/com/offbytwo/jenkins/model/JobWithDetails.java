/*
 * Copyright (c) 2012 Cosmin Stejerean.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import java.util.List;

public class JobWithDetails extends Job {
    String displayName;
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

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<Build> getBuilds() {
        return builds;
    }

    public void setBuilds(List<Build> builds) {
        this.builds = builds;
    }

    public Build getLastBuild() {
        return lastBuild;
    }

    public void setLastBuild(Build lastBuild) {
        this.lastBuild = lastBuild;
    }

    public Build getLastCompletedBuild() {
        return lastCompletedBuild;
    }

    public void setLastCompletedBuild(Build lastCompletedBuild) {
        this.lastCompletedBuild = lastCompletedBuild;
    }

    public Build getLastFailedBuild() {
        return lastFailedBuild;
    }

    public void setLastFailedBuild(Build lastFailedBuild) {
        this.lastFailedBuild = lastFailedBuild;
    }

    public Build getLastStableBuild() {
        return lastStableBuild;
    }

    public void setLastStableBuild(Build lastStableBuild) {
        this.lastStableBuild = lastStableBuild;
    }

    public Build getLastSuccessfulBuild() {
        return lastSuccessfulBuild;
    }

    public void setLastSuccessfulBuild(Build lastSuccessfulBuild) {
        this.lastSuccessfulBuild = lastSuccessfulBuild;
    }

    public Build getLastUnstableBuild() {
        return lastUnstableBuild;
    }

    public void setLastUnstableBuild(Build lastUnstableBuild) {
        this.lastUnstableBuild = lastUnstableBuild;
    }

    public Build getLastUnsuccessfulBuild() {
        return lastUnsuccessfulBuild;
    }

    public void setLastUnsuccessfulBuild(Build lastUnsuccessfulBuild) {
        this.lastUnsuccessfulBuild = lastUnsuccessfulBuild;
    }

    public int getNextBuildNumber() {
        return nextBuildNumber;
    }

    public void setNextBuildNumber(int nextBuildNumber) {
        this.nextBuildNumber = nextBuildNumber;
    }

    public List<Job> getDownstreamProjects() {
        return downstreamProjects;
    }

    public void setDownstreamProjects(List<Job> downstreamProjects) {
        this.downstreamProjects = downstreamProjects;
    }

    public List<Job> getUpstreamProjects() {
        return upstreamProjects;
    }

    public void setUpstreamProjects(List<Job> upstreamProjects) {
        this.upstreamProjects = upstreamProjects;
    }
}
