package com.edu.virtuallab.experiment.dao;

import com.edu.virtuallab.experiment.model.ExperimentScene;
import java.util.List;

public interface ExperimentSceneDao {
    int insert(ExperimentScene scene);
    int update(ExperimentScene scene);
    int delete(Long id);
    ExperimentScene selectById(Long id);
    List<ExperimentScene> selectAll();
} 