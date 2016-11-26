package com.offbytwo.jenkins.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.View;

@Test(groups = { Groups.NO_EXECUTOR_GROUP })
public class NoExecutorStartedGetViewIT extends AbstractJenkinsIntegrationCase {

    private Map<String, View> views;

    @BeforeMethod
    public void beforeMethod() throws IOException {
        views = jenkinsServer.getViews();
    }

    @Test
    public void numberOfViewsIsEqualOne() {
        assertThat(views).hasSize(2);
    }

    @Test
    public void viewNameShouldBeTestView() {
        assertThat(views.containsKey("Test-View")).isTrue();
        assertThat(views.containsKey("All")).isTrue();
    }

    @Test
    public void getJobsFromGetViews() throws IOException {
        List<Job> jobs = views.get("Test-View").getJobs();
        assertThat(jobs).hasSize(1);
    }

    @Test
    public void getJobsViaView() throws IOException {
        View view = jenkinsServer.getView("Test-View");
        List<Job> jobs = view.getJobs();
        assertThat(jobs).hasSize(1);
    }

    @Test
    public void getJobsViaViewWithDetails() throws IOException {
        View view = jenkinsServer.getView("Test-View");
        List<Job> jobs = view.getJobs();
        assertThat(jobs).hasSize(1);

        Job job = jobs.get(0);
        assertThat(job.getName()).isEqualTo("test");
        assertThat(job.details()).isNotNull();
    }
}
