/**
 *
 */
package com.offbytwo.jenkins.model;

import java.util.List;

/**
 * @author johuang
 *
 */
public class StageFlowNodes extends Stage {
    List<Integer> parentNodes;
    StageFlowNodesLog log;
    public List<Integer> getParentNodes() {
        return parentNodes;
    }
    public void setParentNodes(List<Integer> parentNodes) {
        this.parentNodes = parentNodes;
    }
    public StageFlowNodesLog getLog() {
        return log;
    }
    public void setLog(StageFlowNodesLog log) {
        this.log = log;
    }
}
