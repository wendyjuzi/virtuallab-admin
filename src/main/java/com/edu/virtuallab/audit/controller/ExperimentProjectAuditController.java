package com.edu.virtuallab.audit.controller;

import com.edu.virtuallab.audit.model.ExperimentProjectAuditLog;
import com.edu.virtuallab.audit.service.ExperimentProjectAuditService;
import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.experiment.model.ExperimentProject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experiment/project/audit")
@Api(tags = "实验项目审核管理")
public class ExperimentProjectAuditController {

    private final ExperimentProjectAuditService auditService;

    @Autowired
    public ExperimentProjectAuditController(ExperimentProjectAuditService auditService) {
        this.auditService = auditService;
    }

    @ApiOperation("提交实验项目审核")
    @PostMapping("/submit/{projectId}")
    @PreAuthorize("hasAuthority('experiment:project:create')")
    public CommonResult<String> submitForAudit(
            @PathVariable Long projectId,
            @RequestAttribute Long userId) {

        auditService.submitForAudit(projectId, userId);
        return CommonResult.success("实验项目已提交审核");
    }

    @ApiOperation("审核实验项目")
    @PostMapping("/audit/{projectId}")
    @PreAuthorize("hasAuthority('experiment:project:approve')")
    public CommonResult<String> auditProject(
            @PathVariable Long projectId,
            @RequestParam String status, // approved/rejected
            @RequestParam(required = false) String comment,
            @RequestAttribute Long auditorId) {

        auditService.auditProject(projectId, status, comment, auditorId);
        return CommonResult.success("实验项目审核操作已完成");
    }

    @ApiOperation("发布实验项目到班级")
    @PostMapping("/publish/{projectId}")
    @PreAuthorize("hasAuthority('experiment:project:publish')")
    public CommonResult<String> publishProject(
            @PathVariable Long projectId,
            @RequestBody List<Long> classIds,
            @RequestAttribute Long publisherId) {

        auditService.publishProject(projectId, classIds, publisherId);
        return CommonResult.success("实验项目已成功发布到指定班级");
    }

    @ApiOperation("获取待审核实验项目列表")
    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('experiment:project:approve')")
    public CommonResult<List<ExperimentProject>> getPendingProjects() {
        List<ExperimentProject> projects = auditService.getPendingProjects();
        return CommonResult.success(projects);
    }

    @ApiOperation("获取实验项目审核历史")
    @GetMapping("/history/{projectId}")
    public CommonResult<List<ExperimentProjectAuditLog>> getAuditHistory(
            @PathVariable Long projectId) {

        List<ExperimentProjectAuditLog> history = auditService.getAuditHistory(projectId);
        return CommonResult.success(history);
    }

    @ApiOperation("获取已发布班级")
    @GetMapping("/published-classes/{projectId}")
    public CommonResult<List<Long>> getPublishedClasses(
            @PathVariable Long projectId) {

        List<Long> classIds = auditService.getPublishedClasses(projectId);
        return CommonResult.success(classIds);
    }
}