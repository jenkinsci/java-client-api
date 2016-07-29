package com.offbytwo.jenkins.model;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class MavenJobWithDetails extends MavenJob {

    private String displayName;
    private boolean buildable;
    private List<MavenBuild> builds;
    private MavenBuild lastBuild;
    private MavenBuild lastCompletedBuild;
    private MavenBuild lastFailedBuild;
    private MavenBuild lastStableBuild;
    private MavenBuild lastSuccessfulBuild;
    private MavenBuild lastUnstableBuild;
    private MavenBuild lastUnsuccessfulBuild;
    private int nextBuildNumber;
    private List<Job> downstreamProjects;
    private List<Job> upstreamProjects;

    public MavenJobWithDetails() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isBuildable() {
        return buildable;
    }

    public List<MavenBuild> getBuilds() {
        return Lists.transform(builds, new Function<MavenBuild, MavenBuild>() {
            @Override
            public MavenBuild apply(MavenBuild from) {
                return buildWithClient(from);
            }
        });
    }

    public MavenBuild getLastBuild() {
        return buildWithClient(lastBuild);
    }

    public MavenBuild getLastCompletedBuild() {
        return buildWithClient(lastCompletedBuild);
    }

    public MavenBuild getLastFailedBuild() {
        return buildWithClient(lastFailedBuild);
    }

    public MavenBuild getLastStableBuild() {
        return buildWithClient(lastStableBuild);
    }

    public MavenBuild getLastSuccessfulBuild() {
        return buildWithClient(lastSuccessfulBuild);
    }

    public MavenBuild getLastUnstableBuild() {
        return buildWithClient(lastUnstableBuild);
    }

    public MavenBuild getLastUnsuccessfulBuild() {
        return buildWithClient(lastUnsuccessfulBuild);
    }

    public int getNextBuildNumber() {
        return nextBuildNumber;
    }

    public List<Job> getDownstreamProjects() {
        return Lists.transform(downstreamProjects, new MavenJobWithClient());
    }

    public List<Job> getUpstreamProjects() {
        return Lists.transform(upstreamProjects, new MavenJobWithClient());
    }

    private MavenBuild buildWithClient(MavenBuild from) {
        MavenBuild ret = new MavenBuild(from);
        ret.setClient(client);
        return ret;
    }

    private class MavenJobWithClient implements Function<Job, Job> {
        @Override
        public Job apply(Job job) {
            job.setClient(client);
            return job;
        }
    }
}
