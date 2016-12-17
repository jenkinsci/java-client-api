/**
 *
 */
package com.offbytwo.jenkins.model;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author johuang
 *
 */
public class Stage extends BaseModel {
    String id;
    String name;
    String execNode;
    String status;
    Timestamp startTimeMillis;
    long durationMillis;
    long pauseDurationMillis;
    List<StageFlowNodes> stageFlowNodes;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getExecNode() {
        return execNode;
    }
    public void setExecNode(String execNode) {
        this.execNode = execNode;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Timestamp getStartTimeMillis() {
        return startTimeMillis;
    }
    public void setStartTimeMillis(Timestamp startTimeMillis) {
        this.startTimeMillis = startTimeMillis;
    }
    public long getDurationMillis() {
        return durationMillis;
    }
    public void setDurationMillis(long durationMillis) {
        this.durationMillis = durationMillis;
    }
    public long getPauseDurationMillis() {
        return pauseDurationMillis;
    }
    public void setPauseDurationMillis(long pauseDurationMillis) {
        this.pauseDurationMillis = pauseDurationMillis;
    }

    public List<StageFlowNodes> getStageFlowNodes() {
        return stageFlowNodes;
    }
    public void setStageFlowNodes(List<StageFlowNodes> stageFlowNodes) {
        this.stageFlowNodes = stageFlowNodes;
    }
    @Override
    public String toString() {
        return "Stage [id=" + id + ", name=" + name + ", execNode=" + execNode + ", status=" + status
                + ", startTimeMillis=" + startTimeMillis + ", durationMillis=" + durationMillis
                + ", pauseDurationMillis=" + pauseDurationMillis + ", stageFlowNodes=" + stageFlowNodes + "]";
    }
}
