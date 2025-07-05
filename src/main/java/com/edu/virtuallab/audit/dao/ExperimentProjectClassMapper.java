package com.edu.virtuallab.audit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.virtuallab.experiment.model.ExperimentProjectClass;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface ExperimentProjectClassMapper extends BaseMapper<ExperimentProjectClass> {

    @Delete("DELETE FROM experiment_project_class WHERE project_id = #{projectId}")
    int deleteByProjectId(Long projectId);

    @Insert("<script>" +
            "INSERT INTO experiment_project_class (project_id, class_id) VALUES " +
            "<foreach collection='classIds' item='classId' separator=','>" +
            "(#{projectId}, #{classId})" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("projectId") Long projectId, @Param("classIds") List<Long> classIds);

    @Select("SELECT class_id FROM experiment_project_class WHERE project_id = #{projectId}")
    List<Long> selectClassIdsByProjectId(Long projectId);
    @Select("SELECT class_id FROM experiment_project_class WHERE project_id = #{projectId}")
    List<Long> findClassIdsByProjectId(Long projectId); // 确保此方法存在
}