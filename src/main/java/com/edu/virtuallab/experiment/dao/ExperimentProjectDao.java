package com.edu.virtuallab.experiment.dao;

import com.edu.virtuallab.experiment.model.ExperimentProject;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExperimentProjectDao {
    int insert(ExperimentProject project);
    int update(ExperimentProject project);
    int delete(Long id);
    ExperimentProject selectById(Long id);
    List<ExperimentProject> selectAll();
} 