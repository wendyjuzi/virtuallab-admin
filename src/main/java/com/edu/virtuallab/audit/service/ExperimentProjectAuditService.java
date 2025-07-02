package com.edu.virtuallab.audit.service;

import com.edu.virtuallab.audit.dao.ExperimentProjectAuditLogMapper;
import com.edu.virtuallab.audit.dao.ExperimentProjectClassMapper;
import com.edu.virtuallab.audit.dao.ExperimentProjectMapper;
import com.edu.virtuallab.audit.model.ExperimentProjectAuditLog;
import com.edu.virtuallab.common.exception.BusinessException;
import com.edu.virtuallab.experiment.model.ExperimentProject;
import com.edu.virtuallab.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExperimentProjectAuditService {
    @Autowired
    private ExperimentProjectMapper projectMapper;
    @Autowired
    private ExperimentProjectAuditLogMapper auditLogMapper;
    @Autowired
    private ExperimentProjectClassMapper projectClassMapper;
    @Autowired
    private NotificationService notificationService;

    @Autowired
    public ExperimentProjectAuditService(
            ExperimentProjectMapper projectMapper,
            ExperimentProjectAuditLogMapper auditLogMapper,
            ExperimentProjectClassMapper projectClassMapper,
            NotificationService notificationService) {
        this.projectMapper = projectMapper;
        this.auditLogMapper = auditLogMapper;
        this.projectClassMapper = projectClassMapper;
        this.notificationService = notificationService;
    }

    /**
     * 提交实验项目审核
     */
//    @Transactional
//    public void submitForAudit(Long projectId, Long userId) {
//        ExperimentProject project = projectMapper.selectById(projectId);
//        if (project == null) {
//            throw new BusinessException("实验项目不存在");
//        }
//        if (!"draft".equals(project.getAuditStatus())) {
//            throw new BusinessException("只有草稿状态的项目可以提交审核");
//        }
//
//        // 更新项目状态
//        project.setAuditStatus("pending");
//        projectMapper.updateById(project);
//
//        // 记录审核日志
//        recordAuditLog(projectId, userId, "draft", "pending", "提交审核");
//
//        // 发送通知给管理员
//        notificationService.sendProjectAuditNotification(projectId, project.getUploaderId());
//    }

    /**
     * 审核实验项目
     */
    @Transactional
    public void auditProject(Long projectId, String status, String comment, Long auditorId) {
        if (!"approved".equals(status) && !"rejected".equals(status)) {
            throw new BusinessException("无效的审核状态");
        }

        ExperimentProject project = projectMapper.selectById(projectId);
        if (project == null) {
            throw new BusinessException("实验项目不存在");
        }
        if (!"pending".equals(project.getAuditStatus())) {
            throw new BusinessException("项目不在待审核状态");
        }

        String fromStatus = project.getAuditStatus();

        // 更新项目状态
        projectMapper.updateAuditStatus(projectId, status, comment, auditorId);

        // 记录审核日志
        recordAuditLog(projectId, auditorId, fromStatus, status, comment);

        // 发送通知给上传者
        notificationService.sendProjectAuditResultNotification(
                projectId,
                "approved".equals(status),
                comment
        );
    }

    /**
     * 发布实验项目到班级
     */
    @Transactional
    public void publishProject(Long projectId, List<Long> classIds, Long publisherId) {
        if (classIds == null || classIds.isEmpty()) {
            throw new BusinessException("请选择要发布的班级");
        }

        ExperimentProject project = projectMapper.selectById(projectId);
        if (project == null) {
            throw new BusinessException("实验项目不存在");
        }
        if (!"approved".equals(project.getAuditStatus())) {
            throw new BusinessException("项目未审核通过");
        }

        // 删除原有的班级关联
        projectClassMapper.deleteByProjectId(projectId);

        // 添加新的班级关联
        projectClassMapper.batchInsert(projectId, classIds);

        // 更新项目发布状态
        projectMapper.publishProject(projectId);

        // 发送通知给相关班级的师生
        notificationService.sendProjectPublishNotification(projectId, classIds);
    }

    /**
     * 获取待审核项目列表
     */
    public List<ExperimentProject> getPendingProjects() {
        return projectMapper.selectPendingProjects();
    }

    /**
     * 获取项目审核历史
     */
    public List<ExperimentProjectAuditLog> getAuditHistory(Long projectId) {
        return auditLogMapper.selectByProjectId(projectId);
    }

    /**
     * 获取已发布班级
     */
    public List<Long> getPublishedClasses(Long projectId) {
        return projectClassMapper.selectClassIdsByProjectId(projectId);
    }

    // 记录审核日志
    private void recordAuditLog(
            Long projectId,
            Long auditorId,
            String fromStatus,
            String toStatus,
            String comment) {

        ExperimentProjectAuditLog log = new ExperimentProjectAuditLog();
        log.setExperimentProjectId(projectId);
        log.setAuditorId(auditorId);
        log.setFromStatus(fromStatus);
        log.setToStatus(toStatus);
        log.setComment(comment);
        log.setCreatedAt(LocalDateTime.now());

        auditLogMapper.insert(log);
    }


    // 新增方法：获取所有实验项目
    public List<ExperimentProject> getAllProjects() {
        return projectMapper.selectAll();
    }

    /**
     * 获取已通过审核的项目列表
     */
    public List<ExperimentProject> getApprovedProjects() {
        return projectMapper.selectApprovedProjects();
    }

    /**
     * 获取已驳回的项目列表
     */
    public List<ExperimentProject> getRejectedProjects() {
        return projectMapper.selectRejectedProjects();
    }
}

