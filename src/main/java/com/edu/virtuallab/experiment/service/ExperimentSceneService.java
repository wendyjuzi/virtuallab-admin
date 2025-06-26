package com.edu.virtuallab.experiment.service;

import com.edu.virtuallab.experiment.model.ExperimentScene;
import java.util.List;

public interface ExperimentSceneService {
    int create(ExperimentScene scene);
    int update(ExperimentScene scene);
    int delete(Long id);
    ExperimentScene getById(Long id);
    List<ExperimentScene> listAll();
} 