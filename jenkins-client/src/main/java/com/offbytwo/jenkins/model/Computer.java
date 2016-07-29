/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import java.io.IOException;
import java.util.List;

import com.google.common.net.UrlEscapers;

/**
 * @author Kelly Plummer
 *
 */
public class Computer extends BaseModel {

    private String displayName;

    public List<Computer> getComputers() {
        return computer;
    }

    public void setComputer(List<Computer> computer) {
        this.computer = computer;
    }

    List<Computer> computer;

    public Computer() {
    }

    public Computer(String displayName) {
        this();
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public ComputerWithDetails details() throws IOException {
        String name;
        if ("master".equals(displayName)) {
            name = "(master)";
        } else {
            name = UrlEscapers.urlPathSegmentEscaper().escape(displayName);
        }
        // TODO: Check if depth=2 is a good idea or if it could be solved
        // better.
        ComputerWithDetails computerWithDetails = client.get("/computer/" + name + "/?depth=2",
                ComputerWithDetails.class);
        computerWithDetails.setClient(client);
        return computerWithDetails;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Computer other = (Computer) obj;
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
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((computer == null) ? 0 : computer.hashCode());
        result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
        return result;
    }

}
