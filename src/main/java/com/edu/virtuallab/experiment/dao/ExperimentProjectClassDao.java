package com.edu.virtuallab.experiment.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExperimentProjectClassDao {
    int insert(@Param("projectId") Long projectId, @Param("classId") Long classId);
//    List<Long> findStudentIdsByClassIds(List<Long> classIds);

    @Select("SELECT class_id FROM experiment_project_class WHERE project_id = #{projectId}")
    List<Long> findClassIdsByProjectId(Long projectId);


    @Select({
            "<script>",
            "SELECT DISTINCT project_id FROM experiment_project_class",
            "WHERE class_id IN",
            "<foreach item='classId' collection='classIds' open='(' separator=',' close=')'>",
            "#{classId}",
            "</foreach>",
            "</script>"
    })
    List<Long> findProjectIdsByClassIds(@Param("classIds") List<Long> classIds);
}
