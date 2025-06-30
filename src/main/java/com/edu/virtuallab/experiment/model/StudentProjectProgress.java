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
}


