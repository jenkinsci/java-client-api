package com.offbytwo.jenkins.integration;

import com.offbytwo.jenkins.JenkinsServer;
import org.junit.Before;
import org.junit.Rule;
import org.jvnet.hudson.test.JenkinsRule;

public class BaseForIntegrationTests {

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule();
    protected JenkinsServer jenkinsServer;

    @Before
    public void setUp() throws Exception {
        jenkinsRule.getInstance().getCrumbIssuer().getCrumb();
        jenkinsRule.getInstance().getCrumbIssuer().getCrumbRequestField();
        jenkinsServer = new JenkinsServer(jenkinsRule.getURL().toURI());
    }

}
