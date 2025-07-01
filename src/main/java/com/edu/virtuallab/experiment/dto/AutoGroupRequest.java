package com.edu.virtuallab.experiment.dto;

public class AutoGroupRequest {

    private Long projectId;
    private int groupSize;

    public AutoGroupRequest() {
    }

    public AutoGroupRequest(Long projectId, int groupSize) {
        this.projectId = projectId;
        this.groupSize = groupSize;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    @Override
    public String toString() {
        return "AutoGroupRequest{" +
                "projectId=" + projectId +
                ", groupSize=" + groupSize +
                '}';
    }
}
