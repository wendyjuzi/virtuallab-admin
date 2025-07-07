package com.edu.virtuallab.audit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.virtuallab.audit.dto.AuditLogDTO;
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


    @Select("<script>" +
            "SELECT log.id, p.name AS project_name, auditor.real_name AS auditor_name, " +
            "       log.from_status, log.to_status, log.comment, log.created_at " +
            "FROM experiment_project_audit_log log " +
            "JOIN experiment_project p ON log.experiment_project_id = p.id " +
            "JOIN user auditor ON log.auditor_id = auditor.id " +
            "WHERE p.created_by IN " +
            "   <foreach item='username' collection='usernames' open='(' separator=',' close=')'>" +
            "       #{username} " +
            "   </foreach> " +
            "   <if test='keyword != null and keyword != \"\"'> " +
            "       AND (p.name LIKE CONCAT('%', #{keyword}, '%') " +
            "        OR auditor.real_name LIKE CONCAT('%', #{keyword}, '%') " +
            "        OR log.comment LIKE CONCAT('%', #{keyword}, '%')) " +
            "   </if> " +
            "ORDER BY log.created_at DESC " +
            "LIMIT #{offset}, #{pageSize}" + // 添加分页限制
            "</script>")
    List<AuditLogDTO> findAuditLogsByDepartment(
            @Param("usernames") List<String> usernames,
            @Param("keyword") String keyword,
            @Param("offset") long offset,  // 添加偏移量参数
            @Param("pageSize") long pageSize);  // 添加每页大小参数

    @Select("<script>" +
            "SELECT COUNT(*) " +
            "FROM experiment_project_audit_log log " +
            "JOIN experiment_project p ON log.experiment_project_id = p.id " +
            "JOIN user auditor ON log.auditor_id = auditor.id " +
            "WHERE p.created_by IN " +
            "   <foreach item='username' collection='usernames' open='(' separator=',' close=')'>" +
            "       #{username} " +
            "   </foreach> " +
            "   <if test='keyword != null and keyword != \"\"'> " +
            "       AND (p.name LIKE CONCAT('%', #{keyword}, '%') " +
            "        OR auditor.real_name LIKE CONCAT('%', #{keyword}, '%') " +
            "        OR log.comment LIKE CONCAT('%', #{keyword}, '%')) " +
            "   </if> " +
            "</script>")
    long countAuditLogsByDepartment(
            @Param("usernames") List<String> usernames,
            @Param("keyword") String keyword);
}
