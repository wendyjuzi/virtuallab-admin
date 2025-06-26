package com.edu.virtuallab.experiment.model;

public class ExperimentRecord {
    private Long id;
    private Long studentId;
    private Long projectId;
    private String operationLog;
    private String data;
    // getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public String getOperationLog() { return operationLog; }
    public void setOperationLog(String operationLog) { this.operationLog = operationLog; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
} 