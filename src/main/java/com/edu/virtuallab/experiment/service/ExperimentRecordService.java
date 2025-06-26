package com.edu.virtuallab.experiment.service;

import com.edu.virtuallab.experiment.model.ExperimentRecord;
import java.util.List;

public interface ExperimentRecordService {
    int create(ExperimentRecord record);
    int update(ExperimentRecord record);
    int delete(Long id);
    ExperimentRecord getById(Long id);
    List<ExperimentRecord> listAll();
} 