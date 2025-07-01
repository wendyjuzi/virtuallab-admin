package com.edu.virtuallab.audit.controller;

import com.edu.virtuallab.audit.model.ProjectAuditLog;
import com.edu.virtuallab.audit.service.ProjectAuditService;
import com.edu.virtuallab.auth.service.PermissionService;
import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.common.enums.AuditStatus;
import com.edu.virtuallab.common.enums.TargetType;
import com.edu.virtuallab.common.api.ResultCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@Api(tags = "项目审核管理")
public class ProjectAuditController {

    @Autowired
    private ProjectAuditService projectAuditService;
    // 添加权限服务注入
    @Autowired
    private PermissionService permissionService;

    @ApiOperation("提交项目审核")
    @PostMapping("/submit")
    @PreAuthorize("hasAuthority('project:submit')")
    public CommonResult<Void> submitForAudit(
            @RequestParam Long projectId,
            @RequestParam Long submitterId) {

        projectAuditService.submitForAudit(projectId, submitterId);
        // 使用构造方法创建Void结果
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), "项目已提交审核", null);    }

    @ApiOperation("审核项目")
    @PostMapping("/audit")
    @PreAuthorize("hasAuthority('project:audit')")
    public CommonResult<Void> auditProject(
            @RequestParam Long auditLogId,
            @RequestParam AuditStatus status,
            @RequestParam(required = false) String comment,
            @RequestParam Long auditorId) {

        projectAuditService.auditProject(auditLogId, status, comment, auditorId);
        // 使用构造方法创建Void结果
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), "项目审核操作已完成", null);    }

    @ApiOperation("获取项目审核历史")
    @GetMapping("/history/{projectId}")
    @PreAuthorize("hasAuthority('project:audit') or @permissionService.hasProjectAccess(#projectId)")
    public CommonResult<List<ProjectAuditLog>> getAuditHistory(
            @PathVariable Long projectId) {

        List<ProjectAuditLog> history = projectAuditService.getAuditHistory(projectId);
        return CommonResult.success(history, "资源更新成功");
    }

    @ApiOperation("发布项目")
    @PostMapping("/publish")
    @PreAuthorize("hasAuthority('project:publish')")
    public CommonResult<Void> publishProject(
            @RequestParam Long projectId,
            @RequestParam TargetType targetType,
            @RequestBody List<Long> targetIds,
            @RequestParam Long publisherId) {

        projectAuditService.publishProject(projectId, targetType, targetIds, publisherId);
        // 使用构造方法创建Void结果
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), "项目已成功发布", null);    }

    @ApiOperation("获取待审核项目列表")
    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('project:audit')")
    public CommonResult<List<ProjectAuditLog>> getPendingAudits() {
        // 这里需要实现获取待审核项目的方法
        // List<ProjectAuditLog> pending = projectAuditService.getPendingAudits();
        // return CommonResult.success(pending);
        // 保持原实现（返回带数据的类型）
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), "待实现", null);
    }
}
