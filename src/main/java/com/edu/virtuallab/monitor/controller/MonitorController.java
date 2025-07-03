package com.edu.virtuallab.monitor.controller;

import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.log.model.OperationLog;
import com.edu.virtuallab.log.service.OperationLogService;
import com.edu.virtuallab.monitor.dto.SystemStatusDTO;
import com.edu.virtuallab.monitor.service.MonitorService;
import com.edu.virtuallab.notification.model.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/monitor")
@RequiredArgsConstructor
public class MonitorController {

    private final MonitorService monitorService;
    private final OperationLogService operationLogService;

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

    // 后续权限使用统计、用户行为分析等接口可追加
}
