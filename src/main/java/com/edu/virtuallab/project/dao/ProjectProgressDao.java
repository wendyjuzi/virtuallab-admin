package com.edu.virtuallab.project.dao;

import com.edu.virtuallab.project.model.ProjectProgress;
import java.util.List;

public interface ProjectProgressDao {
    int insert(ProjectProgress progress);
    int update(ProjectProgress progress);
    int delete(Long id);
    ProjectProgress selectById(Long id);
    List<ProjectProgress> selectAll();
} 