package com.edu.virtuallab.experiment.dao;

import com.edu.virtuallab.experiment.model.ExperimentReportTemplate;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExperimentReportTemplateDao {
    int insert(ExperimentReportTemplate template);
} 