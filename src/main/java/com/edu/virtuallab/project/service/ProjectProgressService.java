package com.edu.virtuallab.project.service;

import com.edu.virtuallab.project.model.ProjectProgress;
import java.util.List;

public interface ProjectProgressService {
    int create(ProjectProgress progress);
    int update(ProjectProgress progress);
    int delete(Long id);
    ProjectProgress getById(Long id);
    List<ProjectProgress> listAll();
} 