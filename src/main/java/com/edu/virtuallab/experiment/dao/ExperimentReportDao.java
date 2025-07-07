package com.edu.virtuallab.experiment.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.virtuallab.experiment.model.ExperimentReport;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.parameters.P;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface ExperimentReportDao extends BaseMapper<ExperimentReport> {
    @Select("SELECT * FROM experiment_report WHERE session_id = #{sessionId}")
    @Results(id = "reportMap", value = {
            @Result(column = "session_id", property = "sessionId"),
            @Result(column = "student_id", property = "studentId"),
            @Result(column = "project_id", property = "projectId"),
            @Result(column = "manual_content", property = "manualContent"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt"),
            @Result(column = "status", property = "status"),
            @Result(column = "principle", property = "principle"),
            @Result(column = "purpose", property = "purpose"),
            @Result(column = "category", property = "category"),
            @Result(column = "method", property = "method"),
            @Result(column = "steps", property = "steps"),
            @Result(column = "description", property = "description"),
            @Result(column = "attachment_path", property = "attachmentPath"),
            @Result(column = "original_filename", property = "originalFilename"),
            @Result(column = "file_size", property = "fileSize"),
            @Result(column = "mime_type", property = "mimeType"),
            @Result(column = "comment", property = "comment"),
            @Result(column = "score", property = "score")
    })
    ExperimentReport findBySessionId(@Param("sessionId") String sessionId);

    @Update("UPDATE experiment_report SET manual_content = #{content}, status = 'SAVED', updated_at = NOW() WHERE session_id = #{sessionId}")
    int updateManualContent(@Param("sessionId") String sessionId,
                            @Param("content") String content,
                            @Param("status") ExperimentReport.Status status
    );

    @Update("UPDATE experiment_report SET " +
            "attachment_path='', original_filename='', " +
            "file_size=0, mime_type='', updated_at=now() " +
            "WHERE session_id=#{sessionId}")
    void deleteAttachment(String sessionId);

    @Update("UPDATE experiment_report SET status = 'SUBMITTED', updated_at = NOW() WHERE session_id = #{sessionId} AND status IN ('DRAFT', 'SAVED')")
    int submitBySessionId(@Param("sessionId") String sessionId,
                          @Param("status") ExperimentReport.Status status
    );

    @Update("UPDATE experiment_report SET status = 'GRADED', comment = #{comment}, score = #{score} WHERE session_id = #{sessionId} AND status = 'DRAFT'")
    int gradeBySessionId(@Param("sessionId") String sessionId,
                    @Param("status") ExperimentReport.Status status,
                    @Param("comment") String comment,
                    @Param("score") BigDecimal score
    );

    @Select("SELECT * FROM experiment_report WHERE student_id = #{studentId} ORDER BY updated_at DESC")
    @ResultMap("reportMap")
    List<ExperimentReport> findByStudentId(@Param("studentId")Long studentId);

    @Select("SELECT * FROM experiment_report WHERE status IN ('SUBMITTED', 'GRADED')")
    List<ExperimentReport> findSubmittedAndGradedReports();
}