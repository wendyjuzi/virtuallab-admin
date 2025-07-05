package com.edu.virtuallab.experiment.dao;

import com.edu.virtuallab.experiment.model.ExperimentReportTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ExperimentReportTemplateDao {
    int insert(ExperimentReportTemplate template);
    int update(ExperimentReportTemplate template);
    int deleteById(@Param("id") Long id);
    ExperimentReportTemplate findById(@Param("id") Long id);
    List<ExperimentReportTemplate> findByExperimentId(@Param("experimentId") Long experimentId);
    List<ExperimentReportTemplate> findAll();
} 