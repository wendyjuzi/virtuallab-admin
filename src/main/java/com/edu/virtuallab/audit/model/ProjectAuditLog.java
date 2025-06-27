package com.edu.virtuallab.audit.model;

import com.baomidou.mybatisplus.annotation.*;
import com.edu.virtuallab.common.enums.AuditStatus;
import lombok.Data;

import java.util.Date;

@Data
@TableName("project_audit_log")
public class ProjectAuditLog {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("project_id")
    private Long projectId;

    @TableField("auditor_id")
    private Long auditorId;

    @TableField("from_status")
    private AuditStatus fromStatus;

    @TableField("to_status")
    private AuditStatus toStatus;

    private String comment;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private Date createdAt;
}
