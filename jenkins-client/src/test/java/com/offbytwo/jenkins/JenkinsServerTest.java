/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.client.JenkinsHttpConnection;
import com.offbytwo.jenkins.model.FolderJob;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.MainView;
import com.offbytwo.jenkins.model.View;
import java.net.URI;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JenkinsServerTest extends BaseUnitTest {

    private JenkinsHttpConnection client = mock(JenkinsHttpClient.class);
    private JenkinsServer server = new JenkinsServer(client);
    private MainView mainView = new MainView(new Job("Hello", "http://localhost/job/Hello/"));

    @Before
    public void setUp() throws Exception {
        given(client.get("/", MainView.class)).willReturn(mainView);
    }

    @Test
    public void shouldReturnListOfJobs() throws Exception {
        assertTrue(server.getJobs().containsKey("Hello"));
    }

    @Test
    public void testGetJobXml() throws Exception {
        // given
        String xmlString = "<xml>some xml goes here</xml>";
        String jobName = "pr";

        given(client.get(anyString())).willReturn(xmlString);

        // when
        String xmlReturn = server.getJobXml(jobName);

        // then
        verify(client).get("/job/pr/config.xml");
        assertEquals(xmlString, xmlReturn);
    }

    @Test
    public void testFolderGetJobs() throws Exception {

        String[] jobNames = { "job-the-first", "Job-The-Next", "Job-the-Next" };
        // given
        String path = "http://localhost/jobs/someFolder/";
        Job someJob = new Job("jobname", path + "jobname");
        FolderJob folderJob = new FolderJob("someFolder", path);

        List<Job> someJobs = createTestJobs(path, jobNames);
        MainView mv = createTestView(someJobs);

        given(client.get(eq(path), eq(MainView.class))).willReturn(mv);

        // when
        Map<String, Job> map = server.getJobs(folderJob);

        // then
        verify(client).get(path, MainView.class);

        for (String name : jobNames)
            assertTrue(someJobs.contains(map.get(name)));

        assertEquals(jobNames.length, map.size());
    }

    @Test
    public void testFolderGetJob() throws Exception {
        // given
        String path = "http://localhost/jobs/someFolder/";
        JobWithDetails someJob = mock(JobWithDetails.class);
        FolderJob folderJob = new FolderJob("someFolder", path);

        given(client.get(eq(path + "job/jobname"), eq(JobWithDetails.class))).willReturn(someJob);

        // when
        Job jobResult = server.getJob(folderJob, "jobname");

        // then
        verify(client).get(path + "job/jobname", JobWithDetails.class);
        assertEquals(someJob, jobResult);
    }

    @Test
    public void testFolderGetView() throws Exception {
        // given
        String path = "http://localhost/jobs/someFolder/";
        FolderJob folderJob = new FolderJob("someFolder", path);
        View someView = mock(View.class);

        given(client.get(eq(path + "view/viewname/"), eq(View.class))).willReturn(someView);

        // when
        View viewResult = server.getView(folderJob, "viewname");

        // then
        verify(client).get(path + "view/viewname/", View.class);
        assertEquals(someView, viewResult);
    }

    @Test
    public void testGetFolderJob() throws Exception {
        // given
        String path = "http://localhost/jobs/someFolder/";
        Job someJob = new Job("someFolder", path);
        FolderJob folderJob = mock(FolderJob.class);

        given(folderJob.isFolder()).willReturn(true);
        given(client.get(eq(path), eq(FolderJob.class))).willReturn(folderJob);

        // when
        Optional<FolderJob> oj = server.getFolderJob(someJob);

        // then
        verify(client).get(path, FolderJob.class);
        assertEquals(folderJob, oj.get());
    }

    @Test
    public void testGetFolderJobInvalidFolder() throws Exception {
        // given
        String path = "http://localhost/jobs/someFolder/";
        Job someJob = new Job("someFolder", path);
        FolderJob folderJob = mock(FolderJob.class);

        given(folderJob.isFolder()).willReturn(false);
        given(client.get(eq(path), eq(FolderJob.class))).willReturn(folderJob);

        // when
        Optional<FolderJob> oj = server.getFolderJob(someJob);

        // then
        verify(client).get(path, FolderJob.class);
        assertEquals(false, oj.isPresent());
    }

    @Test
    public void testCreateFolderJob() throws Exception {
        // when
        server.createFolder("someFolder");

        // then
        verify(client).post_form(eq("/createItem?"), anyMap(), eq(false));
    }

    @Test
    public void testCreateSubFolderJob() throws Exception {
        // given
        String path = "http://localhost/jobs/someFolder/";
        FolderJob folderJob = mock(FolderJob.class);

        given(folderJob.getUrl()).willReturn(path);

        // when
        server.createFolder(folderJob, "someFolder");

        // then
        verify(client).post_form(eq(path + "createItem?"), anyMap(), eq(false));
    }

    @Test
    public void testUpdateJobXml() throws Exception {
        // given
        String jobName = "pr";
        String xmlString = "<xml>some xml goes here</xml>";

        given(client.post_xml(anyString(), eq(xmlString))).willReturn(xmlString);

        // when
        server.updateJob(jobName, xmlString);

        // then
        ArgumentCaptor<String> captureString = ArgumentCaptor.forClass(String.class);
        verify(client).post_xml(eq("/job/pr/config.xml"), captureString.capture(), eq(true));
        assertEquals(xmlString, captureString.getValue());
    }

    @Test
    public void testCreateJob() throws Exception {
        // given
        String jobName = "test-job-" + UUID.randomUUID().toString();
        String xmlString = "<xml>some xml goes here</xml>";

        // when
        server.createJob(jobName, xmlString);

        // then
        ArgumentCaptor<String> captureString = ArgumentCaptor.forClass(String.class);
        verify(client).post_xml(eq("/createItem?name=" + jobName), captureString.capture(), eq(false));
        String xmlReturn = captureString.getValue();
        assertEquals(xmlReturn, xmlString);
    }

    @Test
    public void testJenkinsConnectivity() throws IOException {
        // given
        given(client.get("/")).willReturn("<xml>not a real response</xml>");

        // then
        assertEquals(server.isRunning(), true);
    }

    @Test
    public void testJenkinsConnectivityBroken() throws IOException {
        // given
        given(client.get("/")).willThrow(IOException.class);

        // then
        assertEquals(server.isRunning(), false);
    }

    @Test
    public void testQuietDown() throws IOException {
        server.quietDown();
        server.cancelQuietDown();
    }

    @Test
    public void testScriptPosts() throws IOException, URISyntaxException {
        given(client.post_text("/scriptText", "script=script", ContentType.APPLICATION_FORM_URLENCODED, false))
                .willReturn("result");
        String result = server.runScript("script");
        verify(client).post_text("/scriptText", "script=script", ContentType.APPLICATION_FORM_URLENCODED, false);
        assertEquals("result", result);
    }

    public void testJenkinsPathEncoding() throws IOException {
        given(client.get("/job/encoded%2Fproperly%3F/config.xml")).willReturn("<xml>not a real response</xml>");

        assertEquals("<xml>not a real response</xml>", server.getJobXml("encoded/properly?"));
    }

    private MainView createTestView(List<Job> jobs) {
        return new MainView(jobs.toArray(new Job[0]));
    }

    private MainView createTestView(String baseUrl, String... jobNames) {
        return createTestView(createTestJobs(baseUrl, jobNames));
    }

    private List<Job> createTestJobs(String baseUrl, String... jobNames) {
        List<Job> jobs = new ArrayList<Job>();
        for (String name : jobNames) {
            jobs.add(new Job(name, baseUrl + name));
        }

        return jobs;
    }

    @Test
    public void testReturnSingleJob() throws Exception {
        shouldReturnListOfJobs("hello");
    }

    @Test
    public void testReturnListOfJobs() throws Exception {
        shouldReturnListOfJobs("hello", "Hello", "HeLLo");
    }

    @Test
    public void testFolderGetSingleJob() throws Exception {
        shouldGetFolderJobs("jobname");
    }

    private void shouldReturnListOfJobs(String... jobNames) throws Exception {
        MainView mainView = createTestView("http://localhost/job/", jobNames);
        given(client.get("/", MainView.class)).willReturn(mainView);
        Map<String, Job> jobs = server.getJobs();
        for (String name : jobNames)
            assertTrue(jobs.containsKey(name));

        assertEquals(jobNames.length, jobs.size());
    }

    @Test
    public void testGetJobXmls() throws Exception {

        shouldGetJobXml("pr");
        shouldGetJobXml("hello");
        shouldGetJobXml("Hello");
        shouldGetJobXml("HeLLo");
    }

    @Test
    public void getVersionShouldNotFailWithNPE()
        throws Exception
    {
        when (client.get( "/" )).thenReturn( "TheAnswer");
        when (client.getJenkinsVersion()).thenReturn( "1.23");
        
        JenkinsServer server = new JenkinsServer( client);
        server.getVersion();
        verify( client, times( 1 )).isJenkinsVersionSet();
        verify( client, times( 1 )).get( "/" );
        verify( client, times( 1 )).getJenkinsVersion();
        
    }
    
    
    @Test(expected=IllegalStateException.class)
    public void testClose() throws Exception {
        final String uri = "http://localhost/jenkins";
        JenkinsServer srv = new JenkinsServer(new URI(uri));
        srv.close();
        srv.close(); //check multiple calls yield no errors
        srv.getComputers();
    }
    
    
  
    
    
    private void shouldGetFolderJobs(String... jobNames) throws IOException {
        // given
        String path = "http://localhost/jobs/someFolder/";
        FolderJob folderJob = new FolderJob("someFolder", path);

        List<Job> someJobs = createTestJobs(path, jobNames);
        MainView mv = createTestView(someJobs);

        given(client.get(eq(path), eq(MainView.class))).willReturn(mv);

        // when
        Map<String, Job> map = server.getJobs(folderJob);

        // then
        verify(client).get(path, MainView.class);

        for (String name : jobNames)
            assertTrue(someJobs.contains(map.get(name)));

        assertEquals(jobNames.length, map.size());
    }

    private void shouldGetJobXml(String jobName) throws Exception {
        // given
        String xmlString = "<xml>some xml goes here</xml>";

        given(client.get(anyString())).willReturn(xmlString);

        // when
        String xmlReturn = server.getJobXml(jobName);

        // then
        verify(client).get("/job/" + jobName + "/config.xml");
        verify(client).get("/job/" + jobName + "/config.xml");
        assertEquals(xmlString, xmlReturn);
    }
}
