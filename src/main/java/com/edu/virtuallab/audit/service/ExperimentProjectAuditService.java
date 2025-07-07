package com.edu.virtuallab.audit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
     * 发布实验项目到班级（使用已关联的班级）
     */
    @Transactional
    public void publishProject(Long projectId) { // 移除 classIds 和 publisherId 参数
        // 1. 获取已关联的班级ID
        List<Long> classIds = projectClassMapper.findClassIdsByProjectId(projectId);

        if (classIds == null || classIds.isEmpty()) {
            throw new BusinessException("项目未关联任何班级，无法发布");
        }

        ExperimentProject project = projectMapper.selectById(projectId);
        if (project == null) {
            throw new BusinessException("实验项目不存在");
        }
        if (!"approved".equals(project.getAuditStatus())) {
            throw new BusinessException("项目未审核通过");
        }

        // 2. 不再需要删除和重新添加关联，因为关联已存在
        // 3. 更新项目发布状态
        projectMapper.publishProject(projectId);

        // 4. 发送通知给相关班级的师生
        notificationService.sendProjectPublishNotification(projectId, classIds);
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

    /**
     * 提交实验项目审核（从draft改为pending）
     */
    @Transactional
    public void submitForAudit(Long projectId) {
        ExperimentProject project = projectMapper.selectById(projectId);
        if (project == null) {
            throw new BusinessException("实验项目不存在");
        }
        if (!"draft".equals(project.getAuditStatus())) {
            throw new BusinessException("只有草稿状态的项目才能提交审核");
        }

        // 更新项目状态为pending
        projectMapper.updateAuditStatusToPending(projectId);

        // +++ 新增: 发送通知给管理员 +++
        notificationService.sendProjectAuditNotification(projectId, project.getCreatedBy());
    }

    public Page<ExperimentProject> getAllProjects(
            String keyword,
            String department,
            int pageNum,
            int pageSize) {

        return projectMapper.selectAll(
                new Page<>(pageNum, pageSize),
                keyword,
                department
        );
    }

    public Page<ExperimentProject> getApprovedProjects(
            String keyword,
            String department,
            int pageNum,
            int pageSize) {

        return projectMapper.selectApprovedProjects(
                new Page<>(pageNum, pageSize),
                keyword,
                department
        );
    }

    public Page<ExperimentProject> getRejectedProjects(
            String keyword,
            String department,
            int pageNum,
            int pageSize) {

        return projectMapper.selectRejectedProjects(
                new Page<>(pageNum, pageSize),
                keyword,
                department
        );
    }

    public Page<ExperimentProject> getPendingProjects(
            String keyword,
            String department,
            int pageNum,
            int pageSize) {

        return projectMapper.selectPendingProjects(
                new Page<>(pageNum, pageSize),
                keyword,
                department
        );
    }

    private String determineDepartment(String departmentFilter, String currentUserDepartment) {
        if (departmentFilter != null && !departmentFilter.isEmpty()) {
            return departmentFilter;
        }
        return currentUserDepartment;
    }

}

