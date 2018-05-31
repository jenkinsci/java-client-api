/*
 * Copyright (c) 2018 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */
package com.offbytwo.jenkins.model;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import static com.google.common.collect.Lists.transform;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Model Class for Maven Modules
 * 
 * @author Jakub Zacek
 */
public class MavenModuleWithDetails extends BaseModel {

    private List<Build> builds;
    private List actions;
    private String displayName;
    private BuildResult result;
    private String url;
    private long duration;
    private long timestamp;

    public List getActions() {
        return actions;
    }

    public void setActions(List actions) {
        this.actions = actions;
    }

    public void setBuilds(List<Build> builds) {
        this.builds = builds;
    }

    public List<Build> getBuilds() {
        if (builds == null) {
            return Collections.emptyList();
        } else {
            return transform(builds, new Function<Build, Build>() {
                @Override
                public Build apply(Build from) {
                    return buildWithClient(from);
                }
            });            
        }
    }
    
    public Build getBuildByNumber(final int buildNumber) {

        Predicate<Build> isMatchingBuildNumber = new Predicate<Build>() {
            @Override
            public boolean apply(Build input) {
                return input.getNumber() == buildNumber;
            }
        };        

        Optional<Build> optionalBuild = Iterables.tryFind(builds, isMatchingBuildNumber);
        // TODO: Check if we could use Build#NO...instead of Null?
        return optionalBuild.orNull() == null ? null : buildWithClient(optionalBuild.orNull());
    }    

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public BuildResult getResult() {
        return result;
    }

    public void setResult(BuildResult result) {
        this.result = result;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getConsoleOutputText() throws IOException {
        return client.get(getUrl() + "/logText/progressiveText");
    }

    public TestReport getTestReport() throws IOException {
        return client.get(this.getUrl() + "/testReport/?depth=1", TestReport.class);
    }

    private Build buildWithClient(Build from) {
        Build ret = from;
        if (from != null) {
            ret = new Build(from);
            ret.setClient(client);
        }
        return ret;
    }

}
