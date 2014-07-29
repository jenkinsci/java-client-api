/*
 * Copyright (c) 2013 Rising Oak LLC.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import com.google.common.base.Predicate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Collections2.filter;

public class BuildWithDetails extends Build {
    List actions;
    boolean building;
    String description;
    int duration;
    int estimatedDuration;
    String fullDisplayName;
    String id;
    long timestamp;
    BuildResult result;
    List<Artifact> artifacts;
    String consoleOutputText;
    String consoleOutputHtml;

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public boolean isBuilding() {
        return building;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public int getEstimatedDuration() {
        return estimatedDuration;
    }

    public String getFullDisplayName() {
        return fullDisplayName;
    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public BuildResult getResult() {
        return result;
    }

    public List getActions() {
        return actions;
    }

    public Map<String, String> getParameters() {
        Collection parameters = filter(actions, new Predicate<Map<String, Object>>() {
            @Override
            public boolean apply(Map<String, Object> action) {
                return action.containsKey("parameters");
            }
        });

        Map<String, String> params = new HashMap<String, String>();

        if (parameters != null && !parameters.isEmpty()) {
            for (Map<String, String> param : ((Map<String, List<Map<String, String>>>) parameters.toArray()[0]).get("parameters")) {
                String key = param.get("name");
                String value = param.get("value");
                params.put(key, value);
            }
        }

        return params;
    }

    public String getConsoleOutputText() throws IOException {
        return client.get(url + "/logText/progressiveText");
    }

    public String getConsoleOutputHtml() throws IOException {
        return client.get(url + "/logText/progressiveHtml");
    }

    public InputStream downloadArtifact(Artifact a) throws IOException, URISyntaxException {
        //We can't just put the artifact's relative path at the end of the url string,
        //as there could be characters that need to be escaped.
        URI uri = new URI(getUrl());
        String artifactPath = uri.getPath() + "artifact/" + a.getRelativePath();
        URI artifactUri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), artifactPath, "", "");
        return client.getFile(artifactUri);
    }
}
