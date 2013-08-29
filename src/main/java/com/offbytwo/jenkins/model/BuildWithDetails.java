/*
 * Copyright (c) 2012 Cosmin Stejerean.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import static com.google.common.collect.Collections2.filter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpResponseException;

import com.google.common.base.Predicate;

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
    
    public InputStream downloadArtifact(Artifact a) throws HttpResponseException, IOException, URISyntaxException {
    	//We can't just put the artifact's relative path at the end of the url string,
    	//as there could be characters that need to be escaped.
    	URI uri = new URI(getUrl());
    	String artifactPath = uri.getPath()+"artifact/"+a.getRelativePath();
    	URI artifactUri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(),uri.getPort(),artifactPath,"", "");
    	return client.getFile(artifactUri);
    }
}
