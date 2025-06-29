package com.edu.virtuallab.experiment.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("experiment_project")
public class ExperimentProject {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String category;
    private String description;
    private String level;
    private String imageUrl;
    private String videoUrl;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    // 新增审核字段
    private Long uploaderId; // 上传者ID
    private String auditStatus; // 审核状态: draft/pending/approved/rejected
    private String auditComment; // 审核意见
    private Long auditorId; // 审核人ID
    private LocalDateTime auditTime; // 审核时间
    private String publishStatus; // 发布状态: unpublished/published
    private LocalDateTime publishTime; // 发布时间
}
