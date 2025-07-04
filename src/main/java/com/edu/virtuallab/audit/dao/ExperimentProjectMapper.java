package com.edu.virtuallab.audit.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.virtuallab.experiment.model.ExperimentProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ExperimentProjectMapper extends BaseMapper<ExperimentProject> {

    @Select("SELECT * FROM experiment_project WHERE audit_status = 'pending'")
    List<ExperimentProject> selectPendingProjects();

    @Update("UPDATE experiment_project SET audit_status = #{status}, " +
            "audit_comment = #{comment}, auditor_id = #{auditorId}, " +
            "audit_time = NOW() WHERE id = #{id}")
    int updateAuditStatus(Long id, String status, String comment, Long auditorId);

    @Update("UPDATE experiment_project SET publish_status = 'published', " +
            "publish_time = NOW() WHERE id = #{id}")
    int publishProject(Long id);

    // 新增方法：获取所有实验项目
    @Select("SELECT * FROM experiment_project")
    List<ExperimentProject> selectAll();

    @Select("SELECT * FROM experiment_project WHERE audit_status = 'approved'")
    List<ExperimentProject> selectApprovedProjects();

    @Select("SELECT * FROM experiment_project WHERE audit_status = 'rejected'")
    List<ExperimentProject> selectRejectedProjects();

    // 新增方法：将草稿状态的项目提交为待审核状态
    @Update("UPDATE experiment_project " +
            "SET audit_status = 'pending', " +
            "    updated_at = NOW() " +
            "WHERE id = #{projectId} AND audit_status = 'draft'")
    int updateAuditStatusToPending(@Param("projectId") Long projectId);

    // 通过用户名查找项目数量
    @Select({
            "<script>",
            "SELECT COUNT(*) FROM experiment_project",
            "WHERE created_by IN",
            "<foreach item='username' collection='usernames' open='(' separator=',' close=')'>",
            "#{username}",
            "</foreach>",
            "</script>"
    })
    int countProjectsByUsernames(@Param("usernames") List<String> usernames);
}
