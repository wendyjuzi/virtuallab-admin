package com.edu.virtuallab.project.service;

import com.edu.virtuallab.project.model.Project;
import java.util.List;

public interface ProjectService {
    int create(Project project);
    int update(Project project);
    int delete(Long id);
    Project getById(Long id);
    List<Project> listAll();
} 