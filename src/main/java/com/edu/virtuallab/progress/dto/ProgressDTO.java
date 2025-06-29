package com.edu.virtuallab.progress.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学生项目进度数据传输对象
 */
@Data
public class ProgressDTO {
    private Long id;

    private Long studentId;

    private Long projectId;

    private String status;

    private LocalDateTime submitTime;

    private Double score;

    private String comment;
}
