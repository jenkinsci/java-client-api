package com.offbytwo.jenkins;

import java.io.IOException;
import java.util.Map;

import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.QueueItem;
import com.offbytwo.jenkins.model.QueueReference;

/**
 * collection of convenient methods which use methods from {@link JenkinsServer}
 * etc.
 * 
 * @author Karl Heinz Marbaise
 *
 */
public class JenkinsTriggerHelper {

    private JenkinsServer server;

    public JenkinsTriggerHelper(JenkinsServer server) {
        this.server = server;
    }

    /**
     * This method will trigger a build of the given job and will wait until the
     * builds is ended or if the build has been cancelled.
     * 
     * @param jobName The name of the job which should be triggered.
     * @return In case of an cancelled job you will get
     *         {@link BuildWithDetails#getResult()}
     *         {@link BuildResult#CANCELLED}. So you have to check first if the
     *         build result is {@code CANCELLED}.
     * @throws IOException in case of errors.
     * @throws InterruptedException In case of interrupts.
     */
    public BuildWithDetails triggerJobAndWaitUntilFinished(String jobName) throws IOException, InterruptedException {
        return triggerJobAndWaitUntilFinished(jobName, false);
    }

    /**
     * This method will trigger a build of the given job and will wait until the
     * builds is ended or if the build has been cancelled.
     * 
     * @param jobName The name of the job which should be triggered.
     * @param params the job parameters
     * @return In case of an cancelled job you will get
     *         {@link BuildWithDetails#getResult()}
     *         {@link BuildResult#CANCELLED}. So you have to check first if the
     *         build result is {@code CANCELLED}.
     * @throws IOException in case of errors.
     * @throws InterruptedException In case of interrupts.
     */
    public BuildWithDetails triggerJobAndWaitUntilFinished(String jobName, Map<String, String> params)
            throws IOException, InterruptedException {
        return triggerJobAndWaitUntilFinished(jobName, params, false);
    }

    /**
     * This method will trigger a build of the given job and will wait until the
     * builds is ended or if the build has been cancelled.
     * 
     * @param jobName The name of the job which should be triggered.
     * @param params the job parameters
     * @param crumbFlag set to <code>true</code> or <code>false</code>.
     * @return In case of an cancelled job you will get
     *         {@link BuildWithDetails#getResult()}
     *         {@link BuildResult#CANCELLED}. So you have to check first if the
     *         build result is {@code CANCELLED}.
     * @throws IOException in case of errors.
     * @throws InterruptedException In case of interrupts.
     */
    public BuildWithDetails triggerJobAndWaitUntilFinished(String jobName, Map<String, String> params,
            boolean crumbFlag) throws IOException, InterruptedException {
        JobWithDetails job = this.server.getJob(jobName);
        QueueReference queueRef = job.build(params, crumbFlag);

        return triggerJobAndWaitUntilFinished(jobName, queueRef);
    }

    /**
     * This method will trigger a build of the given job and will wait until the
     * builds is ended or if the build has been cancelled.
     * 
     * @param jobName The name of the job which should be triggered.
     * @param crumbFlag set to <code>true</code> or <code>false</code>.
     * @return In case of an cancelled job you will get
     *         {@link BuildWithDetails#getResult()}
     *         {@link BuildResult#CANCELLED}. So you have to check first if the
     *         build result is {@code CANCELLED}.
     * @throws IOException in case of errors.
     * @throws InterruptedException In case of interrupts.
     */
    public BuildWithDetails triggerJobAndWaitUntilFinished(String jobName, boolean crumbFlag)
            throws IOException, InterruptedException {
        JobWithDetails job = this.server.getJob(jobName);
        QueueReference queueRef = job.build(crumbFlag);

        return triggerJobAndWaitUntilFinished(jobName, queueRef);
    }

    /**
     * @param jobName The name of the job.
     * @param queueRef {@link QueueReference}
     * @return In case of an cancelled job you will get
     *         {@link BuildWithDetails#getResult()}
     *         {@link BuildResult#CANCELLED}. So you have to check first if the
     *         build result is {@code CANCELLED}.
     * @throws IOException in case of errors.
     * @throws InterruptedException In case of interrupts.
     */
    private BuildWithDetails triggerJobAndWaitUntilFinished(String jobName, QueueReference queueRef)
            throws IOException, InterruptedException {
        JobWithDetails job;
        job = this.server.getJob(jobName);
        QueueItem queueItem = this.server.getQueueItem(queueRef);
        while (!queueItem.isCancelled() && job.isInQueue()) {
            // TODO: May be we should make this configurable?
            Thread.sleep(200);
            job = this.server.getJob(jobName);
            queueItem = this.server.getQueueItem(queueRef);
        }

        if (queueItem.isCancelled()) {
            // TODO: Check if this is ok?
            // We will get the details of the last build. NOT of the cancelled
            // build, cause there is no information about that available cause
            // it does not exist.
            BuildWithDetails result = new BuildWithDetails(job.getLastBuild().details());
            // TODO: Should we add more information here?
            result.setResult(BuildResult.CANCELLED);
            return result;
        }

        job = this.server.getJob(jobName);
        Build lastBuild = job.getLastBuild();

        boolean isBuilding = lastBuild.details().isBuilding();
        while (isBuilding) {
            // TODO: May be we should make this configurable?
            Thread.sleep(200);
            isBuilding = lastBuild.details().isBuilding();
        }

        return lastBuild.details();
    }
}
