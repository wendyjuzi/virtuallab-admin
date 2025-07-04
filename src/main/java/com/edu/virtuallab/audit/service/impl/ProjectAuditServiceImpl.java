package com.edu.virtuallab.audit.service.impl;

import com.edu.virtuallab.audit.dao.ProjectAuditLogMapper;
import com.edu.virtuallab.audit.model.ProjectAuditLog;
import com.edu.virtuallab.audit.service.ProjectAuditService;
import com.edu.virtuallab.common.enums.AuditStatus;
import com.edu.virtuallab.common.enums.NotificationType;
import com.edu.virtuallab.common.enums.TargetType;
import com.edu.virtuallab.notification.service.NotificationService;
import com.edu.virtuallab.project.model.Project;
import com.edu.virtuallab.project.dao.ProjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
public class ProjectAuditServiceImpl implements ProjectAuditService {

    @Autowired
    private ProjectAuditLogMapper auditLogMapper;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private NotificationService notificationService;

    // 状态转换规则
    private static final Map<AuditStatus, Set<AuditStatus>> ALLOWED_TRANSITIONS = new java.util.HashMap<>();
    static {
        ALLOWED_TRANSITIONS.put(AuditStatus.DRAFT, java.util.Collections.singleton(AuditStatus.PENDING));
        java.util.Set<AuditStatus> pendingSet = new java.util.HashSet<>();
        pendingSet.add(AuditStatus.APPROVED);
        pendingSet.add(AuditStatus.REJECTED);
        ALLOWED_TRANSITIONS.put(AuditStatus.PENDING, pendingSet);
        ALLOWED_TRANSITIONS.put(AuditStatus.REJECTED, java.util.Collections.singleton(AuditStatus.PENDING));
    }

    @Override
    @Transactional
    public void auditProject(Long auditLogId, AuditStatus status, String comment, Long auditorId) {
        ProjectAuditLog auditLog = auditLogMapper.selectById(auditLogId);
        Project project = projectDao.selectById(auditLog.getProjectId());

        // 验证状态转换是否允许
        if (!ALLOWED_TRANSITIONS.getOrDefault(project.getAuditStatus(), java.util.Collections.emptySet()).contains(status)) {
            throw new IllegalStateException("无效的状态转换");
        }

        // 更新审核记录
        auditLog.setToStatus(status);
        auditLog.setComment(comment);
        auditLog.setAuditorId(auditorId);
        auditLogMapper.updateById(auditLog);

        // 更新项目状态
        project.setAuditStatus(status);
        project.setAuditorId(auditorId);
        project.setAuditTime(new Date());
        projectDao.update(project);

        // 发送通知
        NotificationType notifyType = status == AuditStatus.APPROVED ?
                NotificationType.PROJECT_APPROVED : NotificationType.PROJECT_REJECTED;

        // 发送通知 - 使用正确的方法
        notificationService.sendProjectAuditResultNotification(
                project.getId(),
                status == AuditStatus.APPROVED, // 审核是否通过
                comment                         // 审核意见
        );
    }

    // 提交项目审核
    @Override
    public void submitForAudit(Long projectId, Long submitterId){

    }

    // 获取项目审核历史
    @Override
    public List<ProjectAuditLog> getAuditHistory(Long projectId){
        return null;
    }


    // 发布项目
    @Override
    public void publishProject(Long projectId, TargetType targetType, List<Long> targetIds, Long publisherId){

    }
}