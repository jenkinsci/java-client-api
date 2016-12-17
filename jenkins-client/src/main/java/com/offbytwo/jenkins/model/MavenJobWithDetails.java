package com.offbytwo.jenkins.model;

import static com.google.common.collect.Lists.transform;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.offbytwo.jenkins.client.util.EncodingUtils;
import com.offbytwo.jenkins.helper.Range;

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
     * This method will give you back the builds of a particular job.
     * 
     * <b>Note: Jenkins limits the number of results to a maximum of 100 builds
     * which you will get back.</b>. In case you have more than 100 build you
     * won't get back all builds via this method. In such cases you need to use
     * {@link #getAllBuilds()}.
     * 
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
     * This method will give you back all builds which exists independent of the
     * number. You should be aware that this can be much in some cases if you
     * have more than 100 builds which is by default limited by Jenkins
     * {@link #getBuilds()}. This method limits it to particular information
     * which can be later used to get supplemental information about a
     * particular build {@link Build#details()} to reduce the amount of data
     * which needed to be transfered.
     * 
     * @return the list of {@link Build}. In case of no builds have been
     *         executed yet return {@link Collections#emptyList()}.
     * @throws IOException
     *             In case of failure.
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-30238">Jenkins
     *      Issue</a>
     */
    public List<MavenBuild> getAllBuilds() throws IOException {
        String path = "/";

        try {
            List<MavenBuild> builds = client.get(path + "job/" + EncodingUtils.encode(this.getName())
                    + "?tree=allBuilds[number[*],url[*],queueId[*]]", AllMavenBuilds.class).getAllBuilds();

            if (builds == null) {
                return Collections.emptyList();
            } else {
                return transform(builds, new Function<MavenBuild, MavenBuild>() {
                    @Override
                    public MavenBuild apply(MavenBuild from) {
                        return buildWithClient(from);
                    }
                });
            }
        } catch (HttpResponseException e) {
            // TODO: Thinks about a better handling if the job does not exist?
            if (e.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                // TODO: Check this if this is necessary or a good idea?

                return null;
            }
            throw e;
        }

    }
    
    /**
    *
    * <ul>
    * <li>{M,N}: From the M-th element (inclusive) to the N-th element
    * (exclusive).</li>
    * <li>{M,}: From the M-th element (inclusive) to the end.</li>
    * <li>{,N}: From the first element (inclusive) to the N-th element
    * (exclusive). The same as {0,N}.</li>
    * <li>{N}: Just retrieve the N-th element. The same as {N,N+1}.</li>
    * </ul>
    * 
    * <b>Note: At the moment there seemed to be no option to get the number of
    * existing builds for a job. The only option is to get all builds via
    * {@link #getAllBuilds()}.</b>
    * 
    * @param range
    *            {@link Range}
    * @return the list of {@link Build}. In case of no builds have been
    *         executed yet return {@link Collections#emptyList()}.
    * @throws IOException
    *             in case of an error.
    */
   public List<MavenBuild> getAllBuilds(Range range) throws IOException {
       String path = "/" + "job/" + EncodingUtils.encode(this.getName())
               + "?tree=allBuilds[number[*],url[*],queueId[*]]";

       try {
           List<MavenBuild> builds = client.get(path + range.getRangeString(), AllMavenBuilds.class).getAllBuilds();

           if (builds == null) {
               return Collections.emptyList();
           } else {
               return transform(builds, new Function<MavenBuild, MavenBuild>() {
                   @Override
                   public MavenBuild apply(MavenBuild from) {
                       return buildWithClient(from);
                   }
               });
           }
       } catch (HttpResponseException e) {
           // TODO: Thinks about a better handline if the job does not exist?
           if (e.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
               // TODO: Check this if this is necessary or a good idea?

               return null;
           }
           throw e;
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

    public MavenBuild getBuildByNumber(final int buildNumber) {

        Predicate<MavenBuild> isMatchingBuildNumber = new Predicate<MavenBuild>() {

            @Override
            public boolean apply(MavenBuild input) {
                return input.getNumber() == buildNumber;
            }
        };

        Optional<MavenBuild> optionalBuild = Iterables.tryFind(builds, isMatchingBuildNumber);
        // TODO: Check if we could use Build#NO...instead of Null?
        return optionalBuild.orNull() == null ? null : buildWithClient(optionalBuild.orNull());
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
