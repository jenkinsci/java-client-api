package com.offbytwo.jenkins.model;

import java.util.List;

public class MavenModule extends BaseModel {
    
    List<MavenModuleRecord> moduleRecords;
    
    public List<MavenModuleRecord> getModuleRecords() {
        return moduleRecords;
    }

}
