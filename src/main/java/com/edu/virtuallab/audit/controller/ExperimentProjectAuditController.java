package com.edu.virtuallab.audit.controller;

import com.edu.virtuallab.audit.model.ExperimentProjectAuditLog;
import com.edu.virtuallab.audit.service.ExperimentProjectAuditService;
import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.experiment.model.ExperimentProject;
import com.edu.virtuallab.log.annotation.OperationLogRecord;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/experiment/project/audit")
@Api(tags = "实验项目审核管理")
public class ExperimentProjectAuditController {

    private final ExperimentProjectAuditService auditService;

    @Autowired
    public ExperimentProjectAuditController(ExperimentProjectAuditService auditService) {
        this.auditService = auditService;
    }
    //将 @RequestAttribute Long userId 改为 @RequestParam Long userId，这样用户 ID 就可以通过请求参数传递
//    @ApiOperation("提交实验项目审核")
//    @PostMapping("/submit/{projectId}")
//    @PreAuthorize("hasAuthority('experiment:project:create')")
//    public CommonResult<String> submitForAudit(
//            @PathVariable Long projectId,
//            @RequestParam  Long userId) {
//
//        auditService.submitForAudit(projectId, userId);
//        return CommonResult.success("实验项目已提交审核", "资源更新成功");
//    }

    @OperationLogRecord(operation = "CREATE_AUDIT", module = "AUDIT", action = "创建实验审核", description = "用户创建实验审核", permissionCode = "AUDIT_MANAGE")
    @PostMapping("/create")
    public int create(@RequestBody ExperimentProjectAuditLog auditLog) {
        // ... existing code ...
    }

    @ApiOperation("审核实验项目")
    @PostMapping("/audit/{projectId}")
    @PreAuthorize("hasAuthority('experiment:project:approve')")
    public CommonResult<String> auditProject(
            @PathVariable Long projectId,
            @RequestParam String status, // approved/rejected
            @RequestParam(required = false) String comment,
            @RequestParam  Long auditorId) {

        auditService.auditProject(projectId, status, comment, auditorId);
        return CommonResult.success("实验项目审核操作已完成", "资源更新成功");
    }

    @OperationLogRecord(operation = "UPDATE_AUDIT", module = "AUDIT", action = "更新实验审核", description = "用户更新实验审核", permissionCode = "AUDIT_MANAGE")
    @PutMapping("/update")
    public int update(@RequestBody ExperimentProjectAuditLog auditLog) {
        // ... existing code ...
    }

    @OperationLogRecord(operation = "DELETE_AUDIT", module = "AUDIT", action = "删除实验审核", description = "用户删除实验审核", permissionCode = "AUDIT_MANAGE")
    @DeleteMapping("/{id}")
    public int delete(@PathVariable Long id) {
        // ... existing code ...
    }

    @ApiOperation("发布实验项目到班级")
    @PostMapping("/publish/{projectId}")
    @PreAuthorize("hasAuthority('experiment:project:publish')")
    public CommonResult<String> publishProject(
            @PathVariable Long projectId,
            @RequestBody List<Long> classIds,
            @RequestParam  Long publisherId) {

        auditService.publishProject(projectId, classIds, publisherId);
        return CommonResult.success("实验项目已成功发布到指定班级", "资源更新成功");
    }

    @ApiOperation("获取待审核实验项目列表")
    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('experiment:project:approve')")
    public CommonResult<List<ExperimentProject>> getPendingProjects() {
        List<ExperimentProject> projects = auditService.getPendingProjects();
        return CommonResult.success(projects, "资源更新成功");
    }

    @ApiOperation("获取实验项目审核历史")
    @GetMapping("/history/{projectId}")
    public CommonResult<List<ExperimentProjectAuditLog>> getAuditHistory(
            @PathVariable Long projectId) {

        List<ExperimentProjectAuditLog> history = auditService.getAuditHistory(projectId);
        return CommonResult.success(history, "资源更新成功");
    }

    @ApiOperation("获取已发布班级")
    @GetMapping("/published-classes/{projectId}")
    public CommonResult<List<Long>> getPublishedClasses(
            @PathVariable Long projectId) {

        List<Long> classIds = auditService.getPublishedClasses(projectId);
        return CommonResult.success(classIds, "资源更新成功");
    }

    @ApiOperation("获取所有实验项目")
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('experiment:project:view')")
    public CommonResult<List<ExperimentProject>> getAllProjects() {
        List<ExperimentProject> projects = auditService.getAllProjects();
        return CommonResult.success(projects, "资源更新成功");
    }

    @ApiOperation("获取已通过审核的实验项目列表")
    @GetMapping("/approved")
    @PreAuthorize("hasAuthority('experiment:project:view')")
    public CommonResult<List<ExperimentProject>> getApprovedProjects() {
        List<ExperimentProject> projects = auditService.getApprovedProjects();
        return CommonResult.success(projects, "资源更新成功");
    }

    @ApiOperation("获取已驳回的实验项目列表")
    @GetMapping("/rejected")
    @PreAuthorize("hasAuthority('experiment:project:view')")
    public CommonResult<List<ExperimentProject>> getRejectedProjects() {
        List<ExperimentProject> projects = auditService.getRejectedProjects();
        return CommonResult.success(projects, "资源更新成功");
    }
}