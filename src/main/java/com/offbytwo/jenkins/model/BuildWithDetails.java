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
import java.util.*;

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

    public boolean isBuilding() { return building; }

    public List<BuildCause> getCauses() {
        // actions is a List<Map<String, List<Map<String, String ..
        // we have a List[i]["causes"] -> List[BuildCause]
        Collection causes = filter(actions, new Predicate<Map<String, Object>>() {
            @Override
            public boolean apply(Map<String, Object> action) {
                return action.containsKey("causes");
            }
        });

        List<BuildCause> result = new ArrayList<BuildCause>();

        if (causes != null && ! causes.isEmpty()) {
            // The underlying key-value can be either a <String, Integer> or a <String, String>.
            List<Map<String, Object>> causes_blob =
                    ((Map<String, List<Map<String, Object>>>) causes.toArray()[0]).get("causes");
            for( Map<String, Object> cause : causes_blob) {

                BuildCause cause_object = new BuildCause();
                cause_object.setShortDescription((String)cause.get("shortDescription"));
                cause_object.setUpstreamBuild((Integer)cause.get("upstreamBuild"));
                cause_object.setUpstreamProject((String)cause.get("upstreamProject"));
                cause_object.setUpstreamUrl((String)cause.get("upstreamUrl"));
                cause_object.setUserId((String)cause.get("userId"));
                cause_object.setUserName((String)cause.get("userName"));

                result.add(cause_object);
            }
        }

        return result;
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
            for (Map<String, Object> param : ((Map<String, List<Map<String, Object>>>) parameters.toArray()[0]).get("parameters")) {
                String key = (String) param.get("name");
                Object value = param.get("value");
                params.put(key, String.valueOf(value));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        BuildWithDetails that = (BuildWithDetails) o;

        if (building != that.building) return false;
        if (duration != that.duration) return false;
        if (estimatedDuration != that.estimatedDuration) return false;
        if (timestamp != that.timestamp) return false;
        if (actions != null ? !actions.equals(that.actions) : that.actions != null)
            return false;
        if (artifacts != null ? !artifacts.equals(that.artifacts) : that.artifacts != null)
            return false;
        if (consoleOutputHtml != null ? !consoleOutputHtml.equals(that.consoleOutputHtml) : that.consoleOutputHtml != null)
            return false;
        if (consoleOutputText != null ? !consoleOutputText.equals(that.consoleOutputText) : that.consoleOutputText != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (fullDisplayName != null ? !fullDisplayName.equals(that.fullDisplayName) : that.fullDisplayName != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (result != that.result) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result1 = super.hashCode();
        result1 = 31 * result1 + (actions != null ? actions.hashCode() : 0);
        result1 = 31 * result1 + (building ? 1 : 0);
        result1 = 31 * result1 + (description != null ? description.hashCode() : 0);
        result1 = 31 * result1 + duration;
        result1 = 31 * result1 + estimatedDuration;
        result1 = 31 * result1 + (fullDisplayName != null ? fullDisplayName.hashCode() : 0);
        result1 = 31 * result1 + (id != null ? id.hashCode() : 0);
        result1 = 31 * result1 + (int) (timestamp ^ (timestamp >>> 32));
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        result1 = 31 * result1 + (artifacts != null ? artifacts.hashCode() : 0);
        result1 = 31 * result1 + (consoleOutputText != null ? consoleOutputText.hashCode() : 0);
        result1 = 31 * result1 + (consoleOutputHtml != null ? consoleOutputHtml.hashCode() : 0);
        return result1;
    }
}
