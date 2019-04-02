/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.integration;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.Computer;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import hudson.model.BooleanParameterValue;
import hudson.model.Cause;
import hudson.model.FreeStyleProject;
import hudson.model.ParametersAction;
import hudson.model.StringParameterValue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JenkinsServerIT {

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule();

    private static final String JENKINS_MASTER = "master";
    private static final String JENKINS_TEST_JOB = "jenkins-client-test";

    private JenkinsServer server;

    @Before
    public void setUp() throws Exception {
        jenkinsRule.getInstance().getCrumbIssuer().getCrumb();
        jenkinsRule.getInstance().getCrumbIssuer().getCrumbRequestField();
        server = new JenkinsServer(jenkinsRule.getURL().toURI());
    }

    @Test
    public void shouldReturnListOfJobs() throws Exception {
        jenkinsRule.getInstance().createProject(FreeStyleProject.class, JENKINS_TEST_JOB);
        assertTrue(server.getJobs().containsKey(JENKINS_TEST_JOB));
    }

    @Test
    public void shouldReturnBuildsForJob() throws Exception {
        FreeStyleProject trunk = jenkinsRule.getInstance().createProject(FreeStyleProject.class, JENKINS_TEST_JOB);
        for (int i = 0; i < 5; i++)
            trunk.scheduleBuild(0, new Cause.UserIdCause(),
                    new ParametersAction(new StringParameterValue("BUILD NUMBER", "" + i)));

        while (trunk.isInQueue() || trunk.isBuilding()) {
        }

        JobWithDetails job = server.getJobs().get(JENKINS_TEST_JOB).details();
        assertEquals(5, job.getBuilds().get(0).getNumber());
    }

    @Test
    public void shouldReturnBuildStatusForBuild() throws Exception {
        FreeStyleProject pr = jenkinsRule.getInstance().createProject(FreeStyleProject.class, JENKINS_TEST_JOB);

        pr.scheduleBuild2(0, new Cause.UserIdCause(),
                new ParametersAction(new StringParameterValue("REVISION", "foobar"))).get();

        JobWithDetails job = server.getJobs().get(JENKINS_TEST_JOB).details();
        BuildWithDetails build = job.getBuilds().get(0).details();
        assertEquals(BuildResult.SUCCESS, build.getResult());
        assertEquals("foobar", build.getParameters().get("REVISION"));
    }

    @Test
    public void shouldSupportBooleanParameters() throws Exception {
        FreeStyleProject pr = jenkinsRule.getInstance().createProject(FreeStyleProject.class, JENKINS_TEST_JOB);

        pr.scheduleBuild2(0, new Cause.UserIdCause(),
                new ParametersAction(new BooleanParameterValue("someValue", true))).get();

        JobWithDetails job = server.getJobs().get(JENKINS_TEST_JOB).details();
        BuildWithDetails build = job.getBuilds().get(0).details();
        assertEquals(Boolean.TRUE, build.getParameters().get("someValue"));
    }

    @Test
    public void shouldReturnListOfComputers() throws Exception {
        assertTrue(server.getComputers().containsKey(JENKINS_MASTER));
    }

    @Test
    public void shouldReturnDetailOfComputer() throws Exception {
        Map<String, Computer> computers = server.getComputers();
        assertTrue(computers.get(JENKINS_MASTER).details().getDisplayName().equals(JENKINS_MASTER));
    }

    @Test
    public void shouldReturnDetailOfLablel() throws Exception {
        assertTrue(server.getLabel(JENKINS_MASTER).getName().equals(JENKINS_MASTER));
    }

    // Note this test depends upon the xml in job-template.xml being a valid job
    // description for the instance of jenkins you are running.
    @Test
    public void testGetJobXml() throws Exception {
        jenkinsRule.getInstance().createProject(FreeStyleProject.class, JENKINS_TEST_JOB);

        String xmlReturned = server.getJobXml(JENKINS_TEST_JOB);
        assertTrue(xmlReturned.length() > 0);
    }

    @Test
    public void testGetJobByName() throws Exception {
        jenkinsRule.getInstance().createProject(FreeStyleProject.class, JENKINS_TEST_JOB);

        JobWithDetails job = server.getJob(JENKINS_TEST_JOB);

        assertEquals(JENKINS_TEST_JOB, job.getName());
        assertEquals(JENKINS_TEST_JOB, job.getDisplayName());
    }

    @Test
    public void testGetJobByNameDoesntExist() throws Exception {
        final String jobName = "imprettysurethereisnojobwiththisname";
        JobWithDetails job = server.getJob(jobName);
        assertEquals(null, job);
    }

    @Test
    public void testCreateJob() throws Exception {
        final String jobName = "test-job-" + UUID.randomUUID().toString();

        jenkinsRule.getInstance().createProject(FreeStyleProject.class, JENKINS_TEST_JOB);
        String sourceXml = server.getJobXml(JENKINS_TEST_JOB);

        server.createJob(jobName, sourceXml, true);

        Map<String, Job> jobs = server.getJobs();
        assertTrue(jobs.containsKey(jobName));
        JobWithDetails thisJob = jobs.get(jobName).details();
        assertNotNull(thisJob);
        assertTrue(thisJob.getBuilds().size() == 0);
    }

    @Test
    public void shouldBuildAJob() throws Exception {
        final String jobName = "test-job-" + UUID.randomUUID().toString();

        FreeStyleProject project = jenkinsRule.getInstance().createProject(FreeStyleProject.class, jobName);
        assertTrue(jenkinsRule.getInstance().getJobNames().contains(jobName));

        JobWithDetails job = server.getJob(jobName);
        job.build(true);

        while (project.isInQueue() || project.isBuilding()) {
        }

        job = server.getJob(jobName);
        assertTrue(job.getBuilds().size() == 1);
    }

    @Test
    public void testUpdateJob() throws Exception {
        final String description = "test-" + UUID.randomUUID().toString();

        FreeStyleProject freeStyleProject = jenkinsRule.getInstance().createProject(FreeStyleProject.class,
                JENKINS_TEST_JOB);
        freeStyleProject.setDescription(description);

        String sourceXml = server.getJobXml(JENKINS_TEST_JOB);
        String newXml = sourceXml.replaceAll("<description>.*</description>",
                "<description>" + description + "</description>");
        server.updateJob(JENKINS_TEST_JOB, newXml);

        String confirmXml = server.getJobXml(JENKINS_TEST_JOB);
        assertTrue(confirmXml.contains(description));
    }
    
    
    @Test(expected=IllegalStateException.class)
    public void testClose_ReuseAfterClosed() throws Exception {
        final FreeStyleProject proj = jenkinsRule.getInstance().createProject(
                FreeStyleProject.class, JENKINS_TEST_JOB);
        final Map<String, Job> jobs = server.getJobs();
        assertNotNull(jobs.get(proj.getName()));
        server.close();
        server.getJobs();        
    }
    
    
    @Test
    public void testClose_CloseMultipleTimes() throws Exception {
        final FreeStyleProject proj = jenkinsRule.getInstance().createProject(
                FreeStyleProject.class, JENKINS_TEST_JOB);
        final Map<String, Job> jobs = server.getJobs();
        assertNotNull(jobs.get(proj.getName()));
        server.close();
        server.close();
    }
}
