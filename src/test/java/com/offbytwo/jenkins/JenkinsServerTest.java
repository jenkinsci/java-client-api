/*
 * Copyright (c) 2013 Rising Oak LLC.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.MainView;

public class JenkinsServerTest {

    private JenkinsHttpClient client = mock(JenkinsHttpClient.class);
    private JenkinsServer server = new JenkinsServer(client);
    private MainView mainView = new MainView(new Job("Hello", "http://localhost/job/Hello/"));

    public JenkinsServerTest() throws Exception {}

    @Before
    public void setUp() throws Exception {
        when(client.get("/", MainView.class)).thenReturn(mainView);
    }

    @Test
    public void shouldReturnListOfJobs() throws Exception {
        assertTrue(server.getJobs().containsKey("hello"));
    }

    @Test
    public void testGetJobXml() throws Exception {
        String jobName = "pr";
        String xmlString = "<xml>some xml goes here</xml>";

        Mockito.when(client.get(Mockito.anyString())).thenReturn(xmlString);
        String xmlReturn = server.getJobXml(jobName);

        Mockito.verify(client).get("/job/pr/config.xml");

        assertEquals(xmlString, xmlReturn);
    }

    @Test
    public void testUpdateJobXml() throws Exception {
        String jobName = "pr";
        String xmlString = "<xml>some xml goes here</xml>";

        Mockito.when(client.post_xml(Mockito.anyString(), Mockito.eq(xmlString))).thenReturn(xmlString);
        server.updateJob(jobName, xmlString);

        ArgumentCaptor<String> captureString = ArgumentCaptor.forClass(String.class);
        Mockito.verify(client).post_xml(Mockito.eq("/job/pr/config.xml"), captureString.capture(), Mockito.eq(true));

        assertEquals(xmlString, captureString.getValue());
    }

    @Test
    public void testCreateJob() throws Exception {
        String jobName = "test-job-" + UUID.randomUUID().toString();
        String xmlString = "<xml>some xml goes here</xml>";

        server.createJob(jobName, xmlString);

        ArgumentCaptor<String> captureString = ArgumentCaptor.forClass(String.class);
        Mockito.verify(client).post_xml(Mockito.eq("/createItem?name=" + jobName), captureString.capture());
        String xmlReturn = captureString.getValue();
        assertEquals(xmlReturn, xmlString);
    }

    @Test
    public void testJenkinsConnectivity() throws IOException {
        Mockito.when(client.get("/")).thenReturn("<xml>not a real response</xml>");
        assertEquals(server.isRunning(), true);
    }

    @Test
    public void testJenkinsConnectivityBroken() throws IOException {
        Mockito.when(client.get("/")).thenThrow(IOException.class);
        assertEquals(server.isRunning(), false);
    }
}
