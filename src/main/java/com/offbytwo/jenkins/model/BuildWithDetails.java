/*
 * Copyright (c) 2012 Cosmin Stejerean.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import com.google.common.base.Predicate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpResponseException;

import static com.google.common.collect.Collections2.filter;

public class BuildWithDetails extends Build {
    List actions;
    boolean building;
    String description;
    int duration;
    String fullDisplayName;
    String id;
    long timestamp;
    BuildResult result;
    List<Artifact> artifacts;

    public List<Artifact> getArtifacts() {
		return artifacts;
	}

	public void setArtifacts(List<Artifact> artifacts) {
		this.artifacts = artifacts;
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

	public void setActions(List actions) {
        this.actions = actions;
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
            for(Map<String, String> param : ((Map<String, List<Map<String, String>>>) parameters.toArray()[0]).get("parameters")) {
                String key = param.get("name");
                String value = param.get("value");
                params.put(key, value);
            }
        }

        return params;
    }
    
    public InputStream downloadArtifact(Artifact a) throws HttpResponseException, IOException {
    	String artifactUrl = getUrl()+"artifact/"+a.getRelativePath();
    	return client.getFile(artifactUrl);
    }
}
