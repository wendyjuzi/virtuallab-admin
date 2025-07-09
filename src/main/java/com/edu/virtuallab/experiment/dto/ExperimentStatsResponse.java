package com.edu.virtuallab.experiment.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

@Data
public class ExperimentStatsResponse {
    private Page<StudentExperimentProjectDTO> pageData;
    private long totalExperiments;
    private long completedExperiments;
    private double averageScore;
}