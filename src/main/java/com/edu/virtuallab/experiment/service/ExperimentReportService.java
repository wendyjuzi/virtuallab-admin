package com.edu.virtuallab.experiment.service;

import com.edu.virtuallab.experiment.model.ExperimentReport;
import java.util.List;

public interface ExperimentReportService {
    int create(ExperimentReport report);
    int update(ExperimentReport report);
    int delete(Long id);
    ExperimentReport getById(Long id);
    List<ExperimentReport> listAll();
} 