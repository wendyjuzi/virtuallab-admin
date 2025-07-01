package com.edu.virtuallab.experiment.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.edu.virtuallab.experiment.model.ExperimentReport;
import org.apache.ibatis.annotations.*;

import java.util.Map;

@Mapper
public interface ExperimentReportDao extends BaseMapper<ExperimentReport> {
    @Select("SELECT * FROM experiment_report WHERE session_id = #{sessionId}")
    @Results(id = "reportMap", value = {
            @Result(column = "session_id", property = "sessionId"),
            @Result(column = "student_id", property = "studentId"),
            @Result(column = "manual_content", property = "manualContent"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt"),
            @Result(column = "principle", property = "principle"),
            @Result(column = "purpose", property = "purpose"),
            @Result(column = "category", property = "category"),
            @Result(column = "method", property = "method"),
            @Result(column = "steps", property = "steps"),
            @Result(column = "description", property = "description")
    })
    ExperimentReport findBySessionId(@Param("sessionId") String sessionId);

    @Update("UPDATE experiment_report SET manual_content = #{content}, updated_at = NOW() WHERE session_id = #{sessionId}")
    int updateManualContent(@Param("sessionId") String sessionId, @Param("content") String content);

    @Update("UPDATE experiment_report SET attachment = #{attachment}, updated_at = NOW() WHERE session_id = #{sessionId}")
    int updateAttachment(@Param("sessionId") String sessionId, @Param("attachment") byte[] attachment);

    @Update("UPDATE experiment_report SET status = 'SUBMITTED', updated_at = NOW() WHERE session_id = #{sessionId} AND status = 'DRAFT'")
    int submitBySessionId(@Param("sessionId") String sessionId);

}