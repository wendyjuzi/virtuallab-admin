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
import com.edu.virtuallab.monitor.dto.SystemPerformanceDTO;
import java.util.Arrays;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    public CommonResult<UserBehaviorDTO> getUserBehavior(@RequestParam(required = false) String range) {
        // 默认统计今日
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date endTime = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        Date startTime = cal.getTime();
        UserBehaviorDTO dto = monitorService.getUserBehaviorAnalysis(startTime, endTime);
        return CommonResult.success(dto, "获取用户行为分析成功");
    }

    /**
     * 系统性能监控接口
     */
    @GetMapping("/performance")
    public CommonResult<SystemPerformanceDTO> getSystemPerformance() {
        SystemPerformanceDTO dto = monitorService.getSystemPerformance();
        return CommonResult.success(dto, "获取系统性能数据成功");
    }

    /**
     * 系统性能数据接口（兼容前端路径）
     */
    @GetMapping("/system-status")
    public CommonResult<SystemStatusDTO> getSystemStatusV2() {
        SystemStatusDTO statusDTO = monitorService.getSystemStatus();
        return CommonResult.success(statusDTO, "获取系统性能数据成功");
    }

    /**
     * 获取今日所有登录行为总次数
     */
    @GetMapping("/login-operation/today-count")
    public CommonResult<Integer> getTodayLoginOperationCount() {
        int count = monitorService.getTodayLoginOperationCount();
        return CommonResult.success(count, "获取今日登录行为总次数成功");
    }

    /**
     * 获取今日登录用户数（去重）
     */
    @GetMapping("/login-operation/today-user-count")
    public CommonResult<Integer> getTodayLoginUserCount() {
        int count = monitorService.getTodayLoginUserCount();
        return CommonResult.success(count, "获取今日登录用户数成功");
    }

    /**
     * 详细操作日志分页接口
     */


    /**
     * 实时操作监控接口，返回最近N条详细日志
     */
    @GetMapping("/realtime-operations")
    public CommonResult<List<OperationLog>> getRealtimeOperations(@RequestParam(defaultValue = "10") int limit) {
        List<OperationLog> logs = operationLogService.getLatestLogs(limit);
        return CommonResult.success(logs, "获取实时操作日志成功");
    }

    // 后续权限使用统计、用户行为分析等接口可追加
}
