/*
 * Copyright (c) 2013 Rising Oak LLC.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import java.util.List;

public class LabelWithDetails extends Job {
    String name;

    List actions;
    List clouds;
    String description;
    Integer idleExecutors;
    List nodes;
    String nodeName;
    Boolean offline;
    List tiedJobs;
    Integer totalExecutors;
    List propertiesList;

    public String getName() {
        return name;
    }

    public List getActions() {
        return actions;
    }

    public List getClouds() {
        return clouds;
    }

    public String getDescription() {
        return description;
    }

    public Integer getIdleExecutors() {
        return idleExecutors;
    }

    public List getNodes() {
        return nodes;
    }

    public String getNodeName() {
        return nodeName;
    }

    public Boolean getOffline() {
        return offline;
    }

    public List getTiedJobs() {
        return tiedJobs;
    }

    public Integer getTotalExecutors() {
        return totalExecutors;
    }

    public List getPropertiesList() {
        return propertiesList;
    }
}