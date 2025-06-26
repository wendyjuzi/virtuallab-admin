package com.edu.virtuallab.experiment.model;

public class ExperimentScene {
    private Long id;
    private Long projectId;
    private String params;
    private String tasks;
    // getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public String getParams() { return params; }
    public void setParams(String params) { this.params = params; }
    public String getTasks() { return tasks; }
    public void setTasks(String tasks) { this.tasks = tasks; }
} 