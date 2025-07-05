package com.edu.virtuallab.experiment.service;

import com.edu.virtuallab.experiment.model.ExperimentReportTemplate;
import java.util.List;

public interface ExperimentReportTemplateService {
    ExperimentReportTemplate aiGenerateTemplate(Long experimentId);
    int create(ExperimentReportTemplate template);
    int update(ExperimentReportTemplate template);
    int delete(Long id);
    ExperimentReportTemplate getById(Long id);
    List<ExperimentReportTemplate> getByExperimentId(Long experimentId);
    List<ExperimentReportTemplate> getAll();
} 