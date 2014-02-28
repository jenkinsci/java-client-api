/*
 * Copyright (c) 2013 Rising Oak LLC.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.offbytwo.jenkins.client.JenkinsClientException;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.client.JobNotFoundException;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.Computer;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;

public class JenkinsServerIntegration {

    private static final String JENKINS_MASTER = "master";
    
    private static final String NON_EXISTING_JOB = "imprettysurethereisnojobwiththisname";
    private static final String JENKINS_TEST_JOB = "jenkins-client-test";

    private static final String SAMPLE_JOB_FILE = "sample-job.xml";
    
    private JenkinsHttpClient client;
    private JenkinsServer server;
    private ExecutorService executor;
    private String sampleJobXml = "";
    
    @Before
    public void setUp() throws Exception {
        client = new JenkinsHttpClient(new URI("http://localhost:8080"));
        server = new JenkinsServer(client);
        executor = Executors.newCachedThreadPool();
        sampleJobXml = loadSampleJob();
        setupTestJob();
    }

    private void setupTestJob() throws IOException {
        try {
            server.createJob(JENKINS_TEST_JOB, sampleJobXml);
        } catch (JenkinsClientException e) {
            if (e.getStatusCode() == 400) {
                server.updateJob(JENKINS_TEST_JOB, sampleJobXml);
            } else {
                throw e;
            }
        }
    }

    private String loadSampleJob() throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(SAMPLE_JOB_FILE);
        return IOUtils.toString(is);
    }

    @Test
    public void shouldReturnListOfJobs() {
        assertTrue(server.getJobs().containsKey(JENKINS_TEST_JOB));
    }

    @Test
    public void shouldReturnListOfComputers() {
        assertTrue(server.getComputers().containsKey(JENKINS_MASTER));
    }

    @Test
    public void shouldReturnDetailOfComputer() {
        Map<String, Computer> computers = server.getComputers();
        assertTrue(computers.get(JENKINS_MASTER).details().getDisplayName().equals(JENKINS_MASTER));
    }

    @Test
    public void shouldReturnDetailOfLablel() {
        assertTrue(server.getLabel(JENKINS_MASTER).getName().equals(JENKINS_MASTER));
    }

    @Test
    public void testGetJobXml() {
        String xmlReturned = server.getJobXml(JENKINS_TEST_JOB);
        assertTrue(xmlReturned.length() > 0);
    }

    @Test(expected = JobNotFoundException.class)
    public void getJobXmlForNonExistingJobShouldThrowException() {
        server.getJobXml(NON_EXISTING_JOB);
    }

    @Test
    public void testGetJobByName() {
        JobWithDetails job = server.getJob(JENKINS_TEST_JOB);

        assertEquals(JENKINS_TEST_JOB, job.getName());
        assertEquals(JENKINS_TEST_JOB, job.getDisplayName());
    }

    @Test(expected = JobNotFoundException.class)
    public void testGetJobByNameDoesntExist() {
        final String jobName = NON_EXISTING_JOB;

        server.getJob(jobName);
    }

    @Test
    public void testCreateJob() {
        final String jobName = "test-job-" + UUID.randomUUID().toString();

        server.createJob(jobName, sampleJobXml);

        Map<String, Job> jobs = server.getJobs();
        assertTrue(jobs.containsKey(jobName));
        JobWithDetails thisJob = jobs.get(jobName).details();
        assertNotNull(thisJob);
        assertTrue(thisJob.getBuilds().size() == 0);
    }

    @Test
    public void shouldBuildAJob() throws Exception {
        final String jobName = "test-job-" + UUID.randomUUID().toString();

        server.createJob(jobName, sampleJobXml);
        JobWithDetails job = server.getJob(jobName);

        assertNotNull(job);
        assertTrue(job.getBuilds().size() == 0);
        job.build();

        // wait to see if the job finishes, but with a timeout
        Future<Void> future = executor.submit(new PerformPollingTest(server, jobName));

        // If this times out, either jenkins is slow or our test failed!
        // IME, usually takes about 10-15 seconds
        future.get(30, TimeUnit.SECONDS);

        Build build = server.getJob(jobName).getLastSuccessfulBuild();
        
        assertEquals(BuildResult.SUCCESS, build.details().getResult());
        assertEquals(1, build.getNumber());
    }

    @Test
    public void testUpdateJob() {
        final String description = "test-" + UUID.randomUUID().toString();

        String sourceXml = server.getJobXml(JENKINS_TEST_JOB);
        String newXml = sourceXml.replaceAll("<description>.*</description>", "<description>" + description
                + "</description>");
        server.updateJob(JENKINS_TEST_JOB, newXml);

        String confirmXml = server.getJobXml(JENKINS_TEST_JOB);
        assertTrue(confirmXml.contains(description));

    }

    @Test(expected = JobNotFoundException.class)
    public void updateNonExistingJobShouldThrowException() {
        server.updateJob(NON_EXISTING_JOB, sampleJobXml);

    }

    private class PerformPollingTest implements Callable<Void> {
        private final JenkinsServer server;
        private final String jobName;

        public PerformPollingTest(JenkinsServer server, String jobName) {
            this.server = server;
            this.jobName = jobName;
        }

        public Void call() throws InterruptedException, IOException {
            while (true) {
                Thread.sleep(500);
                JobWithDetails jwd = server.getJob(jobName);

                try {
                    // Throws NPE until the first build succeeds
                    jwd.getLastSuccessfulBuild();
                } catch (NullPointerException e) {
                    continue;
                }
                // build succeeded
                return null;
            }
        }
    }
}
