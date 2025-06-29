package com.edu.virtuallab.progress.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("student_project_progress")
public class StudentProjectProgress {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long studentId;
    private Long projectId;
    private String status;
    private LocalDateTime submitTime;
    private Double score;
    private String comment;
}


