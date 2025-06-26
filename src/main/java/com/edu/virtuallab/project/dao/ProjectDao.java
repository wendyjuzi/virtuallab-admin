package com.edu.virtuallab.project.dao;

import com.edu.virtuallab.project.model.Project;
import java.util.List;

public interface ProjectDao {
    int insert(Project project);
    int update(Project project);
    int delete(Long id);
    Project selectById(Long id);
    List<Project> selectAll();
} 