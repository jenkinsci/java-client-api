/*
 * Copyright (c) 2013 Rising Oak LLC.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

public class ComputerWithDetails extends Job {
    String displayName;

    List actions;
    List executors;
    Boolean idle;
    Boolean jnlp;
    Boolean launchSupported;
    Map loadStatistics;
    Boolean manualLaunchAllowed;
    Map monitorData;
    Integer numExecutors;
    Boolean offline;
    Object  offlineCause;
    String  offlineReason;
    List oneOffExecutors;
    Boolean temporarilyOffline;

    public String getDisplayName() {
        return displayName;
    }

    public List<Map> getActions() {
        return actions;
    }

    public List<Map> getExecutors() {
        return executors;
    }

    public Boolean getIdle() {
        return idle;
    }

    public Boolean getJnlp() {
        return jnlp;
    }

    public Boolean getLaunchSupported() {
        return launchSupported;
    }

    public Map getLoadStatistics() {
        return loadStatistics;
    }

    public Boolean getManualLaunchAllowed() {
        return manualLaunchAllowed;
    }

    public Map<String, Map> getMonitorData() {
        return monitorData;
    }

    public Integer getNumExecutors() {
        return numExecutors;
    }

    public Boolean getOffline() {
        return offline;
    }

    public Object getOfflineCause() {
        return offlineCause;
    }

    public String getOfflineReason() {
        return offlineReason;
    }

    public List<Map> getOneOffExecutors() {
        return oneOffExecutors;
    }

    public Boolean getTemporarilyOffline() {
        return temporarilyOffline;
    }

    private class ComputerWithClient implements Function<Computer, Computer> {
        @Override
        public Computer apply(Computer computer) {
            computer.setClient(client);
            return computer;
        }
    }
}