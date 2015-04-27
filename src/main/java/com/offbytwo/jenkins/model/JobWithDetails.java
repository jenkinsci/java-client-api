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
        Build ret = from;
        if (from != null) {
            ret = new Build(from);
            ret.setClient(client);
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        JobWithDetails that = (JobWithDetails) o;

        if (buildable != that.buildable) return false;
        if (nextBuildNumber != that.nextBuildNumber) return false;
        if (builds != null ? !builds.equals(that.builds) : that.builds != null)
            return false;
        if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null)
            return false;
        if (downstreamProjects != null ? !downstreamProjects.equals(that.downstreamProjects) : that.downstreamProjects != null)
            return false;
        if (lastBuild != null ? !lastBuild.equals(that.lastBuild) : that.lastBuild != null)
            return false;
        if (lastCompletedBuild != null ? !lastCompletedBuild.equals(that.lastCompletedBuild) : that.lastCompletedBuild != null)
            return false;
        if (lastFailedBuild != null ? !lastFailedBuild.equals(that.lastFailedBuild) : that.lastFailedBuild != null)
            return false;
        if (lastStableBuild != null ? !lastStableBuild.equals(that.lastStableBuild) : that.lastStableBuild != null)
            return false;
        if (lastSuccessfulBuild != null ? !lastSuccessfulBuild.equals(that.lastSuccessfulBuild) : that.lastSuccessfulBuild != null)
            return false;
        if (lastUnstableBuild != null ? !lastUnstableBuild.equals(that.lastUnstableBuild) : that.lastUnstableBuild != null)
            return false;
        if (lastUnsuccessfulBuild != null ? !lastUnsuccessfulBuild.equals(that.lastUnsuccessfulBuild) : that.lastUnsuccessfulBuild != null)
            return false;
        if (upstreamProjects != null ? !upstreamProjects.equals(that.upstreamProjects) : that.upstreamProjects != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (buildable ? 1 : 0);
        result = 31 * result + (builds != null ? builds.hashCode() : 0);
        result = 31 * result + (lastBuild != null ? lastBuild.hashCode() : 0);
        result = 31 * result + (lastCompletedBuild != null ? lastCompletedBuild.hashCode() : 0);
        result = 31 * result + (lastFailedBuild != null ? lastFailedBuild.hashCode() : 0);
        result = 31 * result + (lastStableBuild != null ? lastStableBuild.hashCode() : 0);
        result = 31 * result + (lastSuccessfulBuild != null ? lastSuccessfulBuild.hashCode() : 0);
        result = 31 * result + (lastUnstableBuild != null ? lastUnstableBuild.hashCode() : 0);
        result = 31 * result + (lastUnsuccessfulBuild != null ? lastUnsuccessfulBuild.hashCode() : 0);
        result = 31 * result + nextBuildNumber;
        result = 31 * result + (downstreamProjects != null ? downstreamProjects.hashCode() : 0);
        result = 31 * result + (upstreamProjects != null ? upstreamProjects.hashCode() : 0);
        return result;
    }

    private class JobWithClient implements Function<Job, Job> {
        @Override
        public Job apply(Job job) {
            job.setClient(client);
            return job;
        }
    }
}
