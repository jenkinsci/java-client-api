package com.offbytwo.jenkins.model;

public class BuildCause {
   String shortDescription;

   // For upstreams
   String upstreamBuild;
   String upstreamProject;
   String upstreamUrl;

   // For manual kickoffs
   String userId;
   String userName;

   public String getShortDescription() { return shortDescription; }
   public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

   public int getUpstreamBuild() { return Integer.parseInt(upstreamBuild); }
   public void setUpstreamBuild(String upstreamBuild) { this.upstreamBuild = upstreamBuild; }

   public String getUpstreamProject() { return upstreamProject; }
   public void setUpstreamProject(String upstreamProject) { this.upstreamProject = upstreamProject; }

   public String getUpstreamUrl() {return upstreamUrl; }
   public void setUpstreamUrl(String upstreamUrl) { this.upstreamUrl = upstreamUrl; }

   public String getUserId() { return userId; }
   public void setUserId(String userId) { this.userId = userId; }

   public String getUserName() { return userName; }
   public void setUserName(String userName) { this.userName = userName; }
}
