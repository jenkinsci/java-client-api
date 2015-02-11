package com.offbytwo.jenkins.model;

public class BuildCause extends BaseModel {
    String shortDescription;

    // For upstreams
    int upstreamBuild;
    String upstreamProject;
    String upstreamUrl;

    // For manual kickoffs
    String userId;
    String userName;

    public String getShortDescription(){ return shortDescription; }

    public boolean startedByUpstream() { return upstreamProject == null;}
    public int getUpstreamBuild() { return upstreamBuild; }
    public String getUpstreamProject() { return upstreamProject; }
    public String getUpstreamUrl() { return upstreamUrl; }

    public boolean startedManually() { return userId == null; }
    public String getUserId() { return userId; }
    public String getUserName() { return userName; }



}
