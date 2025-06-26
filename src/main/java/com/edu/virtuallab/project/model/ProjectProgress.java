package com.edu.virtuallab.project.model;

public class ProjectProgress {
    private Long id;
    private Long projectId;
    private Long studentId;
    private String progress;
    // getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getProgress() { return progress; }
    public void setProgress(String progress) { this.progress = progress; }
} 