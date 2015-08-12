/*
 * Copyright (c) 2013 Rising Oak LLC.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import java.io.IOException;
import java.util.List;

import com.google.common.net.UrlEscapers;


public class Computer extends BaseModel {

    private String displayName;

    public List<Computer> getComputers() {
        return computer;
    }

    public void setComputer(List<Computer> computer) {
        this.computer = computer;
    }

    List<Computer> computer;

    public Computer() {}

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
        }
        else {
            name = UrlEscapers.urlPathSegmentEscaper().escape(displayName);
        }
        return client.get("/computer/" + name, ComputerWithDetails.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Computer computer1 = (Computer) o;

        if (computer != null ? !computer.equals(computer1.computer) : computer1.computer != null)
            return false;
        if (displayName != null ? !displayName.equals(computer1.displayName) : computer1.displayName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = displayName != null ? displayName.hashCode() : 0;
        result = 31 * result + (computer != null ? computer.hashCode() : 0);
        return result;
    }

}
