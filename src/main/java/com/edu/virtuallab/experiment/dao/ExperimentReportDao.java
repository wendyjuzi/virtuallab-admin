package com.edu.virtuallab.experiment.dao;

import com.edu.virtuallab.experiment.model.ExperimentReport;
import java.util.List;

public interface ExperimentReportDao {
    int insert(ExperimentReport report);
    int update(ExperimentReport report);
    int delete(Long id);
    ExperimentReport selectById(Long id);
    List<ExperimentReport> selectAll();
} 