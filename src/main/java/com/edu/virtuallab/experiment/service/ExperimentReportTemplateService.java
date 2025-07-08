package com.edu.virtuallab.experiment.service;

import com.edu.virtuallab.experiment.model.ExperimentReportTemplate;
import com.edu.virtuallab.experiment.dto.AiTemplateDTO;

public interface ExperimentReportTemplateService {
    ExperimentReportTemplate createManualTemplate(ExperimentReportTemplate template);
    ExperimentReportTemplate createAiTemplate(AiTemplateDTO dto);
} 