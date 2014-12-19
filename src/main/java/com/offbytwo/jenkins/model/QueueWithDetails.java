// Copyright 2014 Vladimir Alyamkin. All Rights Reserved.

package com.offbytwo.jenkins.model;

import com.google.common.base.Predicate;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Collections2.filter;

public class QueueWithDetails extends Queue {

    /** List of parameters and causes */
    List actions;

    /** Status */
    boolean blocked;
    boolean buildable;
    boolean stuck;
    boolean cancelled;

    /** Connected entities */
    Job task;
    Build executable;

    private Build buildWithClient(Build from) {
        // Check for null value first
        if(from == null) {
            return null;
        }

        // Return new value with client being set
        Build ret = new Build(from);
        ret.setClient(client);
        return ret;
    }

    private Job jobWithClient(Job from) {
        // Check for null value first
        if(from == null) {
            return null;
        }

        // Return new value with client being set
        Job ret = new Job(from);
        ret.setClient(client);
        return ret;
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

    public boolean isBlocked() {
        return blocked;
    }

    public boolean isBuildable() {
        return buildable;
    }

    public boolean isStuck() {
        return stuck;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public Job getTask() {
        return jobWithClient(task);
    }

    public Build getExecutable() {
        return buildWithClient(executable);
    }
}
