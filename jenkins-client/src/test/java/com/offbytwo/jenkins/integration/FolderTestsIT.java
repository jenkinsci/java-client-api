package com.offbytwo.jenkins.integration;

import hudson.PluginManager;
import hudson.PluginWrapper;
import hudson.model.UpdateCenter;

import java.util.List;
import java.util.Optional;
import java.util.Vector;

import jdk.nashorn.internal.ir.annotations.Ignore;
import jenkins.model.Jenkins;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.mockito.Mockito;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.FolderJob;
import com.offbytwo.jenkins.model.JobWithDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class FolderTestsIT {

    private final String JENKINS_WITH_DETAILS_TEST_JOB = "build_with_details";

    private JenkinsServer server;

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule();

    @Before
    public void setUp() throws Exception {
        // Install the cloudbees folder plugin
        Jenkins jenkins = jenkinsRule.getInstance();
        PluginManager pm = jenkins.getPluginManager();
        StaplerRequest req = Mockito.mock(StaplerRequest.class);
        StaplerResponse res = Mockito.mock(StaplerResponse.class);
        Vector<String> pluginNames = new Vector<String>();
        pluginNames.add("plugin.cloudbees-folder");

        Mockito.when(req.getParameterNames()).thenReturn(pluginNames.elements());
        Mockito.when(req.getParameter("dynamicLoad")).thenReturn("true");

        UpdateCenter uc = jenkins.getUpdateCenter();
        uc.updateAllSites();

        List<hudson.model.UpdateSite.Plugin> availables = uc.getAvailables();
        List<PluginWrapper> plugins = pm.getPlugins();

        // TODO: need to figure out how to install the cloudbees folder plugin
        // pm.doInstall(req, res);

        jenkins.getCrumbIssuer().getCrumb();
        jenkins.getCrumbIssuer().getCrumbRequestField();
        server = new JenkinsServer(jenkinsRule.getURL().toURI());
    }

    @Ignore // XXX need to fix this test
    @Test
    public void testFolderPluginAPIs() throws Exception {
        server.createFolder("root");
        JobWithDetails root = server.getJob("root");
        assertThat( root).isNotNull();

        java.util.Optional<FolderJob> rootFolder = server.getFolderJob(root);
        assertThat( rootFolder).isPresent();

        server.createFolder(rootFolder.get(), "subfolder");
        JobWithDetails subfolder = server.getJob(rootFolder.get(), "subfolder");
        assertThat( subfolder).isNotNull();

        Optional<FolderJob> subfolderFolder = server.getFolderJob(subfolder);
        assertThat( subfolderFolder).isPresent();

        server.deleteJob("root");
        root = server.getJob("root");
        assertThat( root ).isNull();
    }
}
