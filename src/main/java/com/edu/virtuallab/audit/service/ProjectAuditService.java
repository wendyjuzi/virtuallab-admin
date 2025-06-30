package com.edu.virtuallab.audit.service;

import com.edu.virtuallab.audit.model.ProjectAuditLog;
import com.edu.virtuallab.common.enums.AuditStatus;
import com.edu.virtuallab.common.enums.TargetType;

import java.util.List;

public interface ProjectAuditService {
    // 提交项目审核
    void submitForAudit(Long projectId, Long submitterId);

    // 审核项目
    void auditProject(Long auditLogId, AuditStatus status, String comment, Long auditorId);

    // 获取项目审核历史
    List<ProjectAuditLog> getAuditHistory(Long projectId);

    // 发布项目
    void publishProject(Long projectId, TargetType targetType, List<Long> targetIds, Long publisherId);
}
