package com.edu.virtuallab.experiment.dao;

import com.edu.virtuallab.experiment.model.StudentProjectProgress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface StudentProjectProgressDao {

    // 插入一条进度记录
    void insert(@Param("projectId") Long projectId,
                @Param("studentId") Long studentId,
                @Param("status") String status);

    // 批量插入
    void insertBatch(@Param("list") List<StudentProjectProgress> progressList);

    // 查找某项目下所有学生进度
    List<StudentProjectProgress> findByProjectId(@Param("projectId") Long projectId);

    // 根据项目和学生ID更新评分与评语
    void updateScoreAndComment(@Param("projectId") Long projectId,
                               @Param("studentId") Long studentId,
                               @Param("score") Integer score,
                               @Param("comment") String comment);

    // 删除某个项目的所有进度记录（可选）
    void deleteByProjectId(@Param("projectId") Long projectId);

    @Select("SELECT * FROM student_project_progress WHERE student_id = #{studentId}")
    List<StudentProjectProgress> findByStudentId(@Param("studentId") Long studentId);
}

