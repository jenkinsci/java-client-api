/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class ComputerSet extends BaseModel {
    private int busyExecutors;
    List<ComputerWithDetails> computer;
    private String displayName;
    private int totalExecutors;

    public ComputerSet()
    {
    }

    
    public int getBusyExecutors() {
        return busyExecutors;
    }

    public void setBusyExecutors(int busyExecutors) {
        this.busyExecutors = busyExecutors;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getTotalExecutors() {
        return totalExecutors;
    }

    public void setTotalExecutors(int totalExecutors) {
        this.totalExecutors = totalExecutors;
    }

    public List<ComputerWithDetails> getComputers() {
        return Lists.transform( computer, new Function<ComputerWithDetails, ComputerWithDetails>() {
            @Override
            public ComputerWithDetails apply(ComputerWithDetails computerWithDetails) {
                computerWithDetails.setClient(client);
                return computerWithDetails;
            }
        });
    }

    public void setComputer(List<ComputerWithDetails> computers) {
        this.computer = computers;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + busyExecutors;
        result = prime * result + ((computer == null) ? 0 : computer.hashCode());
        result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
        result = prime * result + totalExecutors;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ComputerSet other = (ComputerSet) obj;
        if (busyExecutors != other.busyExecutors)
            return false;
        if (computer == null) {
            if (other.computer != null)
                return false;
        } else if (!computer.equals(other.computer))
            return false;
        if (displayName == null) {
            if (other.displayName != null)
                return false;
        } else if (!displayName.equals(other.displayName))
            return false;
        if (totalExecutors != other.totalExecutors)
            return false;
        return true;
    }

}
