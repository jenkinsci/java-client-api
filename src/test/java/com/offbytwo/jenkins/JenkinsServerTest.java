/*
 * Copyright (c) 2013 Rising Oak LLC.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins;

import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.MainView;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JenkinsServerTest extends BaseUnitTest {

    private JenkinsHttpClient client = mock(JenkinsHttpClient.class);
    private JenkinsServer server = new JenkinsServer(client);
    private MainView mainView = new MainView(new Job("Hello", "http://localhost/job/Hello/"));

    public JenkinsServerTest() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        given(client.get("/", MainView.class)).willReturn(mainView);
    }

    @Test
    public void shouldReturnListOfJobs() throws Exception {
        assertTrue(server.getJobs().containsKey("hello"));
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
        verify(client).post_xml(eq("/createItem?name=" + jobName), captureString.capture());
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
    public void testJenkinsPathEncoding() throws IOException {
        given(client.get("/job/encoded%2Fproperly%3F/config.xml")).willReturn("<xml>not a real response</xml>");

        assertEquals("<xml>not a real response</xml>", server.getJobXml("encoded/properly?"));
    }
}
