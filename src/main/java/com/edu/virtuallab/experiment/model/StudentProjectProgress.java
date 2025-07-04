package com.edu.virtuallab.experiment.model;

import lombok.Data;
import java.util.Date;

@Data
public class StudentProjectProgress {
    private Long id;
    private Long projectId;
    private Long studentId;
    private String status;
    private Date submitTime;
    private Integer score;
    private String comment;

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}


