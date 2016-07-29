package com.offbytwo.jenkins.model;

import java.util.List;

public class MavenModuleRecord extends BaseModel {

    private List<MavenArtifact> attachedArtifacts;
    private Build parent;
    private MavenArtifact mainArtifact;
    private MavenArtifact pomArtifact;
    private String url;

    public MavenModuleRecord() {
    }

    public List<MavenArtifact> getAttachedArtifacts() {
        return attachedArtifacts;
    }

    public void setAttachedArtifacts(List<MavenArtifact> attachedArtifacts) {
        this.attachedArtifacts = attachedArtifacts;
    }

    public Build getParent() {
        return parent;
    }

    public void setParent(Build parent) {
        this.parent = parent;
    }

    public MavenArtifact getMainArtifact() {
        return mainArtifact;
    }

    public void setMainArtifact(MavenArtifact mainArtifact) {
        this.mainArtifact = mainArtifact;
    }

    public MavenArtifact getPomArtifact() {
        return pomArtifact;
    }

    public void setPomArtifact(MavenArtifact pomArtifact) {
        this.pomArtifact = pomArtifact;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
