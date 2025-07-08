package com.edu.virtuallab.audit.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.virtuallab.experiment.model.ExperimentProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ExperimentProjectMapper extends BaseMapper<ExperimentProject> {


    @Update("UPDATE experiment_project SET audit_status = #{status}, " +
            "audit_comment = #{comment}, auditor_id = #{auditorId}, " +
            "audit_time = NOW() WHERE id = #{id}")
    int updateAuditStatus(Long id, String status, String comment, Long auditorId);

    @Update("UPDATE experiment_project SET publish_status = 'published', " +
            "publish_time = NOW() WHERE id = #{id}")
    int publishProject(Long id);

    // 新增方法：获取所有实验项目
    @Select("<script>" +
            "SELECT p.* FROM experiment_project p " +
            "LEFT JOIN user u ON p.created_by = u.username COLLATE utf8mb4_unicode_ci " +
            "<where>" +
            "   <if test='department != null and department != \"\"'> " +
            "       AND u.department = #{department} COLLATE utf8mb4_unicode_ci " +
            "   </if>" +
            "   <if test='keyword != null and keyword != \"\"'> " +
            "       AND (p.name LIKE CONCAT('%', #{keyword}, '%') " +
            "       OR p.description LIKE CONCAT('%', #{keyword}, '%')) " +
            "   </if>" +
            "</where>" +
            "</script>")
    Page<ExperimentProject> selectAll(Page<ExperimentProject> page,
                                      @Param("keyword") String keyword,
                                      @Param("department") String department);

    @Select("<script>" +
            "SELECT p.* FROM experiment_project p " +
            "LEFT JOIN user u ON p.created_by = u.username COLLATE utf8mb4_unicode_ci " +
            "WHERE p.audit_status = 'approved' " +
            "   <if test='department != null and department != \"\"'> " +
            "       AND u.department = #{department} COLLATE utf8mb4_unicode_ci " +
            "   </if>" +
            "   <if test='keyword != null and keyword != \"\"'> " +
            "       AND (p.name LIKE CONCAT('%', #{keyword}, '%') " +
            "       OR p.description LIKE CONCAT('%', #{keyword}, '%')) " +
            "   </if>" +
            "</script>")
    Page<ExperimentProject> selectApprovedProjects(Page<ExperimentProject> page,
                                                   @Param("keyword") String keyword,
                                                   @Param("department") String department);

    @Select("<script>" +
            "SELECT p.* FROM experiment_project p " +
            "LEFT JOIN user u ON p.created_by = u.username COLLATE utf8mb4_unicode_ci " +
            "WHERE p.audit_status = 'rejected' " +
            "   <if test='department != null and department != \"\"'> " +
            "       AND u.department = #{department} COLLATE utf8mb4_unicode_ci " +
            "   </if>" +
            "   <if test='keyword != null and keyword != \"\"'> " +
            "       AND (p.name LIKE CONCAT('%', #{keyword}, '%') " +
            "       OR p.description LIKE CONCAT('%', #{keyword}, '%')) " +
            "   </if>" +
            "</script>")
    Page<ExperimentProject> selectRejectedProjects(Page<ExperimentProject> page,
                                                   @Param("keyword") String keyword,
                                                   @Param("department") String department);

    @Select("<script>" +
            "SELECT p.* FROM experiment_project p " +
            "LEFT JOIN user u ON p.created_by = u.username COLLATE utf8mb4_unicode_ci " +
            "WHERE p.audit_status = 'pending' " +
            "   <if test='department != null and department != \"\"'> " +
            "       AND u.department = #{department} COLLATE utf8mb4_unicode_ci " +
            "   </if>" +
            "   <if test='keyword != null and keyword != \"\"'> " +
            "       AND (p.name LIKE CONCAT('%', #{keyword}, '%') " +
            "       OR p.description LIKE CONCAT('%', #{keyword}, '%')) " +
            "   </if>" +
            "</script>")
    Page<ExperimentProject> selectPendingProjects(Page<ExperimentProject> page,
                                                  @Param("keyword") String keyword,
                                                  @Param("department") String department);

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

    @Select("<script>" +
            "SELECT p.* FROM experiment_project p " +
            "WHERE p.audit_status = 'approved' " +
            "AND p.id IN " +
            "<foreach item='id' collection='projectIds' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "<if test='keyword != null and keyword != \"\"'> " +
            "   AND (p.name LIKE CONCAT('%', #{keyword}, '%') " +
            "   OR p.description LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "LIMIT #{offset}, #{size}" +
            "</script>")
    List<ExperimentProject> selectApprovedProjectsByIdsManual(
            @Param("projectIds") List<Long> projectIds,
            @Param("keyword") String keyword,
            @Param("offset") long offset,
            @Param("size") long size);

    @Select("<script>" +
            "SELECT COUNT(*) FROM experiment_project p " +
            "WHERE p.audit_status = 'approved' " +
            "AND p.id IN " +
            "<foreach item='id' collection='projectIds' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "<if test='keyword != null and keyword != \"\"'> " +
            "   AND (p.name LIKE CONCAT('%', #{keyword}, '%') " +
            "   OR p.description LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "</script>")
    long countApprovedProjectsByIds(
            @Param("projectIds") List<Long> projectIds,
            @Param("keyword") String keyword);
}
