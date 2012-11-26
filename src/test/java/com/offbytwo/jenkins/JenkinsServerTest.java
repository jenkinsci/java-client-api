/*
 * Copyright (c) 2012 Cosmin Stejerean.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins;

import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.MainView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JenkinsServerTest {
    private JenkinsHttpClient client = mock(JenkinsHttpClient.class);
    private JenkinsServer server = new JenkinsServer(client);
    private MainView mainView = new MainView(new Job("Hello", "http://localhost/job/Hello/"));

    @Before
    public void setUp() throws Exception {
        when(client.get("/", MainView.class)).thenReturn(mainView);
    }

    @Test
    public void shouldReturnListOfJobs() throws Exception {
        assertTrue(server.getJobs().containsKey("hello"));
    }
}
