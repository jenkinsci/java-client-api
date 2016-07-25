package com.offbytwo.jenkins;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import com.offbytwo.jenkins.model.Computer;
import com.offbytwo.jenkins.model.Job;

public class ManualTest {

    private JenkinsServer server;

    @Before
    public void SetUp() throws URISyntaxException {
	server = new JenkinsServer(new URI("http://192.168.0.202:10090/buildserver/"), "admin", "admin");
    }

    @Test
    public void testFolderPluginAPIs() throws Exception {
	Map<String, Job> jobs = server.getJobs();

	for (Entry<String, Job> item : jobs.entrySet()) {
	    System.out.println("Item: " + item.getKey());

	    System.out.println(" -> v:" + item.getValue());
	    Job value = item.getValue();
	    System.out.println(" -> Name:" + value.getName());
	    System.out.println(" -> URL: " + value.getUrl());
	}

    }

    @Test
    public void computerSet() throws IOException {
	Map<String, Computer> computers = server.getComputers();
	for (Entry<String, Computer> computer : computers.entrySet()) {
	    System.out.println("Computer: " + computer.getKey() + " " + computer.getValue().getDisplayName());
	}
    }

}
