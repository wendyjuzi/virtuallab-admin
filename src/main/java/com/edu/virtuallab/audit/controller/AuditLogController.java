package com.edu.virtuallab.audit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.virtuallab.audit.dto.AuditLogDTO;
import com.edu.virtuallab.audit.service.AuditLogService;
import com.edu.virtuallab.common.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audit-logs")
@Api(tags = "审核日志管理")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @Autowired
    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @ApiOperation("获取院系审核日志（分页）")
    @GetMapping
    @PreAuthorize("hasAuthority('audit:log:view')")
    public CommonResult<Page<AuditLogDTO>> getAuditLogs(
            @RequestParam String department, // 院系名称
            @RequestParam(required = false) String keyword, // 搜索关键字
            @RequestParam(defaultValue = "1") int page, // 改为与前端一致的page参数名
            @RequestParam(defaultValue = "10") int size) { // 改为与前端一致的size参数名

        // 使用正确的参数名传递
        Page<AuditLogDTO> pageData = auditLogService.getAuditLogsByDepartment(
                department,
                keyword,
                page,
                size
        );

        return CommonResult.success(pageData, "审核日志查询成功");
    }
}
