package com.offbytwo.jenkins.model;

public class MavenArtifact extends BaseModel {
    
    String artifactId;
    String canonicalName;
    String classifier;
    String fileName;
    String groupId;
    String md5sum;
    String type;
    String version;
    
    public MavenArtifact() {
        
    }
    
    public String getArtifactId() {
        return artifactId;
    }
    
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }
    
    public String getCanonicalName() {
        return canonicalName;
    }
    
    public void setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
    }
    
    public String getClassifier() {
        return classifier;
    }
    
    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getGroupId() {
        return groupId;
    }
    
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    public String getMd5sum() {
        return md5sum;
    }
    
    public void setMd5sum(String md5sum) {
        this.md5sum = md5sum;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
}
