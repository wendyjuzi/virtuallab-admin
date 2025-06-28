package com.edu.virtuallab.experiment.service;

import com.edu.virtuallab.experiment.dto.ExperimentProjectPublishRequest;
import com.edu.virtuallab.experiment.model.ExperimentProject;
import java.util.List;

public interface ExperimentProjectService {
    int create(ExperimentProject project);
    int update(ExperimentProject project);
    int delete(Long id);
    ExperimentProject getById(Long id);
    List<ExperimentProject> listAll();
    List<ExperimentProject> search(String category, String level, String keyword);
    void publishToClasses(Long projectId, List<Long> classIds);
    int publishProject(ExperimentProjectPublishRequest request);

} 