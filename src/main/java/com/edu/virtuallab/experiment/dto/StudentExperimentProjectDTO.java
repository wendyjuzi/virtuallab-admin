package com.edu.virtuallab.experiment.dto;

import com.edu.virtuallab.experiment.model.ExperimentProject;

import java.math.BigDecimal;

public class StudentExperimentProjectDTO extends ExperimentProject {
    private String progressStatus;  // 进度状态
    private BigDecimal score;        // 成绩

    // getter 和 setter
    public String getProgressStatus() { return progressStatus; }
    public void setProgressStatus(String progressStatus) { this.progressStatus = progressStatus; }

    public BigDecimal getScore() { return score; }
    public void setScore(BigDecimal score) { this.score = score; }
}
