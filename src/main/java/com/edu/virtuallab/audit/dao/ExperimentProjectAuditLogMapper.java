package com.edu.virtuallab.audit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.virtuallab.audit.model.ExperimentProjectAuditLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExperimentProjectAuditLogMapper extends BaseMapper<ExperimentProjectAuditLog> {

    @Insert("INSERT INTO experiment_project_audit_log " +
            "(experiment_project_id, auditor_id, from_status, to_status, comment, created_at) " +
            "VALUES (#{projectId}, #{auditorId}, #{fromStatus}, #{toStatus}, #{comment}, NOW())")
    int insertAuditLog(
            @Param("projectId") Long projectId,
            @Param("auditorId") Long auditorId,
            @Param("fromStatus") String fromStatus,
            @Param("toStatus") String toStatus,
            @Param("comment") String comment);

    @Select("SELECT * FROM experiment_project_audit_log " +
            "WHERE experiment_project_id = #{projectId} " +
            "ORDER BY created_at DESC")
    List<ExperimentProjectAuditLog> selectByProjectId(Long projectId);
}
