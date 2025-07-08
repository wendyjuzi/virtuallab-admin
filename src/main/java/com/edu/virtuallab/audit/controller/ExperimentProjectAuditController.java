package com.edu.virtuallab.audit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.virtuallab.audit.model.ExperimentProjectAuditLog;
import com.edu.virtuallab.audit.service.ExperimentProjectAuditService;
import com.edu.virtuallab.auth.dao.UserDao;
import com.edu.virtuallab.auth.model.User;
import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.experiment.model.ExperimentProject;
import com.edu.virtuallab.log.annotation.OperationLogRecord;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private UserDao userDao;

    @Autowired
    public ExperimentProjectAuditController(ExperimentProjectAuditService auditService) {
        this.auditService = auditService;
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
        auditService.publishProject(projectId);

        return CommonResult.success("实验项目审核操作已完成", "资源更新成功");
    }

    @ApiOperation("发布实验项目到班级")
    @PostMapping("/publish/{projectId}")
    @PreAuthorize("hasAuthority('experiment:project:publish')")
    public CommonResult<String> publishProject(@PathVariable Long projectId) {
        auditService.publishProject(projectId);
        return CommonResult.success("实验项目已成功发布到指定班级", "资源更新成功");
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


    @ApiOperation("提交实验项目审核")
    @PostMapping("/submit/{projectId}")
    @PreAuthorize("hasAuthority('experiment:project:edit')")
    public CommonResult<String> submitForAudit(@PathVariable Long projectId) {
        auditService.submitForAudit(projectId);
        return CommonResult.success("实验项目已提交审核", "操作成功");
    }

    @ApiOperation("获取所有实验项目（分页+院系）")
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('experiment:project:view')")
    public CommonResult<Page<ExperimentProject>> getAllProjects(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String department,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "12") int pageSize) {

        Page<ExperimentProject> page = auditService.getAllProjects(
                keyword,
                department,
                pageNum,
                pageSize
        );
        return CommonResult.success(page, "资源更新成功");
    }

    @ApiOperation("获取已通过审核的实验项目列表（分页+院系）")
    @GetMapping("/approved")
    @PreAuthorize("hasAuthority('experiment:project:view')")
    public CommonResult<Page<ExperimentProject>> getApprovedProjects(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String department,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "12") int pageSize) {

        Page<ExperimentProject> page = auditService.getApprovedProjects(
                keyword,
                department,
                pageNum,
                pageSize
        );
        return CommonResult.success(page, "资源更新成功");
    }

    @ApiOperation("获取已驳回的实验项目列表（分页+院系）")
    @GetMapping("/rejected")
    @PreAuthorize("hasAuthority('experiment:project:view')")
    public CommonResult<Page<ExperimentProject>> getRejectedProjects(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String department,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "12") int pageSize) {

        Page<ExperimentProject> page = auditService.getRejectedProjects(
                keyword,
                department,
                pageNum,
                pageSize
        );
        return CommonResult.success(page, "资源更新成功");
    }

    @ApiOperation("获取待审核实验项目列表（分页+院系）")
    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('experiment:project:approve')")
    public CommonResult<Page<ExperimentProject>> getPendingProjects(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String department,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "12") int pageSize) {

        Page<ExperimentProject> page = auditService.getPendingProjects(
                keyword,
                department,
                pageNum,
                pageSize
        );
        return CommonResult.success(page, "资源更新成功");
    }
    private String getCurrentUserDepartment() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userDao.findByUsername(username);
        return currentUser != null ? currentUser.getDepartment() : null;
    }
}