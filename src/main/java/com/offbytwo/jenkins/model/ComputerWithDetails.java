/*
 * Copyright (c) 2013 Rising Oak LLC.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import java.util.List;
import java.util.Map;

import com.google.common.base.Function;

public class ComputerWithDetails extends Computer {

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
    Object offlineCause;
    String offlineReason;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ComputerWithDetails that = (ComputerWithDetails) o;

        if (actions != null ? !actions.equals(that.actions) : that.actions != null)
            return false;
        if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null)
            return false;
        if (executors != null ? !executors.equals(that.executors) : that.executors != null)
            return false;
        if (idle != null ? !idle.equals(that.idle) : that.idle != null)
            return false;
        if (jnlp != null ? !jnlp.equals(that.jnlp) : that.jnlp != null)
            return false;
        if (launchSupported != null ? !launchSupported.equals(that.launchSupported) : that.launchSupported != null)
            return false;
        if (loadStatistics != null ? !loadStatistics.equals(that.loadStatistics) : that.loadStatistics != null)
            return false;
        if (manualLaunchAllowed != null ? !manualLaunchAllowed.equals(that.manualLaunchAllowed) : that.manualLaunchAllowed != null)
            return false;
        if (monitorData != null ? !monitorData.equals(that.monitorData) : that.monitorData != null)
            return false;
        if (numExecutors != null ? !numExecutors.equals(that.numExecutors) : that.numExecutors != null)
            return false;
        if (offline != null ? !offline.equals(that.offline) : that.offline != null)
            return false;
        if (offlineCause != null ? !offlineCause.equals(that.offlineCause) : that.offlineCause != null)
            return false;
        if (offlineReason != null ? !offlineReason.equals(that.offlineReason) : that.offlineReason != null)
            return false;
        if (oneOffExecutors != null ? !oneOffExecutors.equals(that.oneOffExecutors) : that.oneOffExecutors != null)
            return false;
        if (temporarilyOffline != null ? !temporarilyOffline.equals(that.temporarilyOffline) : that.temporarilyOffline != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (actions != null ? actions.hashCode() : 0);
        result = 31 * result + (executors != null ? executors.hashCode() : 0);
        result = 31 * result + (idle != null ? idle.hashCode() : 0);
        result = 31 * result + (jnlp != null ? jnlp.hashCode() : 0);
        result = 31 * result + (launchSupported != null ? launchSupported.hashCode() : 0);
        result = 31 * result + (loadStatistics != null ? loadStatistics.hashCode() : 0);
        result = 31 * result + (manualLaunchAllowed != null ? manualLaunchAllowed.hashCode() : 0);
        result = 31 * result + (monitorData != null ? monitorData.hashCode() : 0);
        result = 31 * result + (numExecutors != null ? numExecutors.hashCode() : 0);
        result = 31 * result + (offline != null ? offline.hashCode() : 0);
        result = 31 * result + (offlineCause != null ? offlineCause.hashCode() : 0);
        result = 31 * result + (offlineReason != null ? offlineReason.hashCode() : 0);
        result = 31 * result + (oneOffExecutors != null ? oneOffExecutors.hashCode() : 0);
        result = 31 * result + (temporarilyOffline != null ? temporarilyOffline.hashCode() : 0);
        return result;
    }

    private class ComputerWithClient implements Function<Computer, Computer> {
        @Override
        public Computer apply(Computer computer) {
            computer.setClient(client);
            return computer;
        }
    }
}