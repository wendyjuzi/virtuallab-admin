package com.edu.virtuallab.score.model;

public class Score {
    private Long id;
    private Long studentId;
    private Long projectId;
    private Double autoScore;
    private Double manualScore;
    private String feedback;
    // getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public Double getAutoScore() { return autoScore; }
    public void setAutoScore(Double autoScore) { this.autoScore = autoScore; }
    public Double getManualScore() { return manualScore; }
    public void setManualScore(Double manualScore) { this.manualScore = manualScore; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
} 