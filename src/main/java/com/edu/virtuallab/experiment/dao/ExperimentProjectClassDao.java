package com.edu.virtuallab.experiment.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExperimentProjectClassDao {
    int insert(@Param("projectId") Long projectId, @Param("classId") Long classId);
//    List<Long> findStudentIdsByClassIds(List<Long> classIds);

}
