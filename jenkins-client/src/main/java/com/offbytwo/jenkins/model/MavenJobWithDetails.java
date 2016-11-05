package com.offbytwo.jenkins.model;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class MavenJobWithDetails extends MavenJob {

    private String displayName;
    private boolean buildable;
    private List<MavenBuild> builds;
    private MavenBuild firstBuild;
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

    /**
     * @return the list of {@link MavenBuild}. In case of no builds have been
     *         executed yet {@link Collections#emptyList()} will be returned.
     */
    public List<MavenBuild> getBuilds() {
        if (builds == null) {
            return Collections.emptyList();
        } else {
            return Lists.transform(builds, new Function<MavenBuild, MavenBuild>() {
                @Override
                public MavenBuild apply(MavenBuild from) {
                    return buildWithClient(from);
                }
            });
        }
    }

    /**
     * @return The firstBuild. If {@link #firstBuild} has never been run
     *         {@link MavenBuild#BUILD_HAS_NEVER_RUN} will be returned.
     */
    public MavenBuild getFirstBuild() {
        if (firstBuild == null) {
            return MavenBuild.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(firstBuild);
        }
    }

    /**
     * @return The lastBuild. If {@link #lastBuild} has never been run
     *         {@link MavenBuild#BUILD_HAS_NEVER_RUN} will be returned.
     */
    public MavenBuild getLastBuild() {
        if (lastBuild == null) {
            return MavenBuild.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastBuild);
        }
    }

    /**
     * @return The lastCompletedBuild. If {@link #lastCompletedBuild} has never
     *         been run {@link MavenBuild#BUILD_HAS_NEVER_RUN} will be returned.
     */
    public MavenBuild getLastCompletedBuild() {
        if (lastCompletedBuild == null) {
            return MavenBuild.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastCompletedBuild);
        }
    }

    /**
     * @return The lastFailedBuild. If {@link #lastFailedBuild} has never been
     *         run {@link MavenBuild#BUILD_HAS_NEVER_RUN} will be returned.
     */
    public MavenBuild getLastFailedBuild() {
        if (lastFailedBuild == null) {
            return MavenBuild.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastFailedBuild);
        }
    }

    /**
     * @return The lastStableBuild. If {@link #lastStableBuild} has never been
     *         run {@link MavenBuild#BUILD_HAS_NEVER_RUN} will be returned.
     */
    public MavenBuild getLastStableBuild() {
        if (lastStableBuild == null) {
            return MavenBuild.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastStableBuild);
        }
    }

    /**
     * @return The lastSuccessfulBuild. If {@link #lastSuccessfulBuild} has
     *         never been run {@link MavenBuild#BUILD_HAS_NEVER_RUN} will be
     *         returned.
     */
    public MavenBuild getLastSuccessfulBuild() {
        if (lastSuccessfulBuild == null) {
            return MavenBuild.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastSuccessfulBuild);
        }
    }

    /**
     * @return The lastUnstableBuild. If {@link #lastUnstableBuild} has never
     *         been run {@link MavenBuild#BUILD_HAS_NEVER_RUN} will be returned.
     */
    public MavenBuild getLastUnstableBuild() {
        if (lastUnstableBuild == null) {
            return MavenBuild.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastUnstableBuild);
        }
    }

    /**
     * @return The lastUnsuccessfulBuild. If {@link #lastUnsuccessfulBuild} has
     *         never been run {@link MavenBuild#BUILD_HAS_NEVER_RUN} will be
     *         returned.
     */
    public MavenBuild getLastUnsuccessfulBuild() {
        if (lastUnsuccessfulBuild == null) {
            return MavenBuild.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastUnsuccessfulBuild);
        }
    }

    public int getNextBuildNumber() {
        return nextBuildNumber;
    }

    public List<Job> getDownstreamProjects() {
        if (downstreamProjects == null) {
            return Collections.emptyList();
        } else {
            return Lists.transform(downstreamProjects, new MavenJobWithClient());
        }
    }

    public List<Job> getUpstreamProjects() {
        if (upstreamProjects == null) {
            return Collections.emptyList();
        } else {
            return Lists.transform(upstreamProjects, new MavenJobWithClient());
        }
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
