package com.edu.virtuallab.project.model;

import com.baomidou.mybatisplus.annotation.*;
import com.edu.virtuallab.common.enums.AuditStatus;
import com.edu.virtuallab.common.enums.PublishStatus;
import com.edu.virtuallab.common.enums.TargetType;
import lombok.Data;

import java.util.Date;

@Data
@TableName("project")
public class Project {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String description;
    private String status;

    // 审核相关字段
    @TableField("uploader_id")
    private Long uploaderId;

    @TableField("audit_status")
    private AuditStatus auditStatus = AuditStatus.DRAFT;

    @TableField("audit_comment")
    private String auditComment;

    @TableField("auditor_id")
    private Long auditorId;

    @TableField("audit_time")
    private Date auditTime;

    // 发布相关字段
    @TableField("publish_status")
    private PublishStatus publishStatus = PublishStatus.UNPUBLISHED;

    @TableField("publish_time")
    private Date publishTime;

    @TableField("target_type")
    private TargetType targetType = TargetType.ALL;

    // 时间戳
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}