package com.edu.virtuallab.audit.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("experiment_project_audit_log")
public class ExperimentProjectAuditLog {
    private Long id;
    private Long experimentProjectId;
    private Long auditorId;
    private String fromStatus;
    private String toStatus;
    private String comment;
    private LocalDateTime createdAt;
}
