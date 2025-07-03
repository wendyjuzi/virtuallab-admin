package com.edu.virtuallab.monitor.controller;

import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.log.model.OperationLog;
import com.edu.virtuallab.log.service.OperationLogService;
import com.edu.virtuallab.monitor.dto.SystemStatusDTO;
import com.edu.virtuallab.monitor.service.MonitorService;
import com.edu.virtuallab.notification.model.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.edu.virtuallab.auth.dao.PermissionDao;
import com.edu.virtuallab.monitor.dto.PermissionStatDTO;
import com.edu.virtuallab.monitor.dto.UserBehaviorDTO;
import com.edu.virtuallab.log.dao.OperationLogDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/monitor")
@RequiredArgsConstructor
public class MonitorController {

    private final MonitorService monitorService;
    private final OperationLogService operationLogService;

    @Autowired(required = false)
    private PermissionDao permissionDao;
    @Autowired(required = false)
    private OperationLogDao operationLogDao;

    /**
     * 统一系统状态接口
     */
    @GetMapping("/status")
    public CommonResult<SystemStatusDTO> getSystemStatus() {
        SystemStatusDTO statusDTO = monitorService.getSystemStatus();
        return CommonResult.success(statusDTO, "获取系统状态成功");
    }

    /**
     * 实时操作监控：获取最新日志
     */
    @GetMapping("/operations/realtime")
    public CommonResult<List<OperationLog>> getRealtimeOperations(@RequestParam(defaultValue = "10") int limit) {
        List<OperationLog> logs = operationLogService.getLatestLogs(limit);
        return CommonResult.success(logs, "获取最新日志成功");
    }

    /**
     * 日志分页查询
     */
    @GetMapping("/operations/logs")
    public CommonResult<Map<String, Object>> getOperationLogs(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type) {

        PageResult<OperationLog> pageResult = operationLogService.queryLogs(page, size, keyword, type);
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        return CommonResult.success(result, "获取日志成功");
    }

    /**
     * 日志详情
     */
    @GetMapping("/operations/logs/{id}")
    public CommonResult<OperationLog> getOperationLogDetail(@PathVariable Long id) {
        OperationLog log = operationLogService.getLogById(id);
        return log != null ? CommonResult.success(log, "获取日志详情成功") : CommonResult.failed("获取日志详情失败");
    }

    /**
     * 权限统计接口
     */
    @GetMapping("/permission-stats")
    public CommonResult<PermissionStatDTO> getPermissionStats() {
        PermissionStatDTO dto = new PermissionStatDTO();
        // 权限总数
        int totalPermissions = 0;
        if (permissionDao != null && permissionDao.findAll() != null) {
            totalPermissions = permissionDao.findAll().size();
        }
        dto.setTotalPermissions(totalPermissions);
        // 各权限分配情况（示例：每个权限被多少角色拥有）
        // 这里只做简单统计，如需更详细可扩展
        // TODO: 可扩展为统计每个权限被多少用户/角色拥有
        dto.setPermissionDistribution(null);
        dto.setRolePermissionCount(null);
        return CommonResult.success(dto, "获取权限统计成功");
    }

    /**
     * 用户行为分析接口
     */
    @GetMapping("/user-behavior")
    public CommonResult<UserBehaviorDTO> getUserBehavior() {
        UserBehaviorDTO dto = new UserBehaviorDTO();
        // 活跃用户数（示例：操作日志中不同用户数）
        int activeUserCount = 0;
        if (operationLogDao != null && operationLogDao.findAll() != null) {
            activeUserCount = (int) operationLogDao.findAll().stream().map(log -> log.getUserId()).distinct().count();
        }
        dto.setActiveUserCount(activeUserCount);
        // 操作类型统计（示例：按operation分组计数）
        // TODO: 可扩展为SQL分组统计
        dto.setOperationTypeStats(null);
        // 每日活跃用户数（示例：不实现，留空）
        dto.setDailyActiveUsers(null);
        return CommonResult.success(dto, "获取用户行为分析成功");
    }

    /**
     * 系统性能数据接口（兼容前端路径）
     */
    @GetMapping("/system-status")
    public CommonResult<SystemStatusDTO> getSystemStatusV2() {
        SystemStatusDTO statusDTO = monitorService.getSystemStatus();
        return CommonResult.success(statusDTO, "获取系统性能数据成功");
    }

    // 后续权限使用统计、用户行为分析等接口可追加
}
