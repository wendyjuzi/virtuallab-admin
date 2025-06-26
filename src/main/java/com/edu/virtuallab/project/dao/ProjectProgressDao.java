package com.edu.virtuallab.project.dao;

import com.edu.virtuallab.project.model.ProjectProgress;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProjectProgressDao {
    int insert(ProjectProgress progress);
    int update(ProjectProgress progress);
    int delete(Long id);
    ProjectProgress selectById(Long id);
    List<ProjectProgress> selectAll();
} 