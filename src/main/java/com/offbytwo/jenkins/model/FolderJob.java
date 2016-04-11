package com.offbytwo.jenkins.model;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.offbytwo.jenkins.client.util.EncodingUtils;

public class FolderJob extends Job {

    String displayName;
    List<Job> jobs;

    public FolderJob() {
    }

    public FolderJob(String name, String url) {
        super(name, url);
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Determine if this FolderJob object is a valid folder or not.
     * 
     * (internally: if jobs list exists)
     * 
     * @return true if this job is a folder.
     */
    public boolean isFolder() {
        if (jobs != null) {
            return true;
        }
        return false;
    }

    /**
     * Get a list of all the defined jobs in this folder
     *
     * @return list of defined jobs (summary level, for details @see Job#details
     * @throws IOException
     */
    public Map<String, Job> getJobs() {
        return Maps.uniqueIndex(jobs, new Function<Job, String>() {
            @Override
            public String apply(Job job) {
                job.setClient(client);
                return job.getName().toLowerCase();
            }
        });
    }

    /**
     * Get a job in this folder by name
     *
     * @return the given job
     * @throws IOException
     */
    public Job getJob(String name) {
        return Maps.uniqueIndex(jobs, new Function<Job, String>() {
            @Override
            public String apply(Job job) {
                job.setClient(client);
                return job.getName().toLowerCase();
            }
        }).get(name);
    }

    /**
     * Create a folder on the server (as a subfolder of this folder)
     *
     * @throws IOException
     */
    public void createFolder(String jobName) throws IOException {
        createFolder(jobName, false);
    }

    /**
     * Create a folder on the server (as a subfolder of this folder)
     *
     * @throws IOException
     */
    public void createFolder(String jobName, Boolean crumbFlag) throws IOException {
        // https://gist.github.com/stuart-warren/7786892 was slightly helpful
        // here
        ImmutableMap<String, String> params = ImmutableMap.of("mode", "com.cloudbees.hudson.plugins.folder.Folder",
                "name", EncodingUtils.encodeParam(jobName), "from", "", "Submit", "OK");
        client.post_form(this.getUrl() + "/createItem?", params, crumbFlag);
    }

    /*
     * TODO public List<Job> getJobsRecursive() { return Lists.transform(jobs,
     * new Function<Job, Job>() {
     * 
     * @Override public Job apply(Job job) { // TODO: try to see if each job is
     * a folder return job; } }); }
     */
}
