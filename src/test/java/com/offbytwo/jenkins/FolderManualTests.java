package com.offbytwo.jenkins;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.Optional;
import com.offbytwo.jenkins.model.FolderJob;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;

public class FolderManualTests {

    private JenkinsServer server;

    @Before
    public void SetUp() throws URISyntaxException {
        // point this at a local instance of jenkins which has the cloudbees
        // folder plugin installed.
        server = new JenkinsServer(new URI("http://localhost:8080/"));
    }

    @Ignore
    @Test
    public void testFolderPluginAPIs() throws Exception {
        server.createFolder("root");
        JobWithDetails root = server.getJob("root");
        Assert.assertNotNull(root);

        Optional<FolderJob> rootFolder = server.getFolderJob(root);
        Assert.assertTrue(rootFolder.isPresent());

        server.createFolder(rootFolder.get(), "subfolder");
        JobWithDetails subfolder = server.getJob(rootFolder.get(), "subfolder");
        Assert.assertNotNull(subfolder);

        Optional<FolderJob> subfolderFolder = server.getFolderJob(subfolder);
        Assert.assertTrue(subfolderFolder.isPresent());

        server.deleteJob("root");
        root = server.getJob("root");
        Assert.assertNull(root);
    }

    @Ignore
    @Test
    public void testStuff() {
        try {

            String f1 = "test-root";
            String f2 = "test-nested";
            String f3 = "test-double-nested";

            server.createFolder(f1);
            Job job1 = server.getJob(f1);
            FolderJob fjob = server.getFolderJob(job1).get();
            server.createFolder(fjob, f2);

            Job job2 = server.getJob(fjob, f2);
            FolderJob fjob2 = server.getFolderJob(job2).get();
            server.createFolder(fjob2, f3);

            Job job3 = server.getJob(fjob2, f3);
            FolderJob fjob3 = server.getFolderJob(job3).get();

            JobWithDetails dj = fjob3.details();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
