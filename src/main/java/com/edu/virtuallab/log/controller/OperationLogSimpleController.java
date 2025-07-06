package com.edu.virtuallab.log.controller;

import com.edu.virtuallab.log.model.OperationLog;
import com.edu.virtuallab.log.service.OperationLogSimpleService;
import com.edu.virtuallab.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/system/logs/simple")
public class OperationLogSimpleController {

    @Autowired
    private OperationLogSimpleService operationLogSimpleService;

    // 增加日志
    @PostMapping("/add")
    public CommonResult<?> addLog(@RequestBody OperationLog log) {
        operationLogSimpleService.add(log);
        return CommonResult.success(null, "日志添加成功");
    }

    // 查看所有日志
    @GetMapping("/list")
    public CommonResult<List<OperationLog>> listLogs() {
        List<OperationLog> logs = operationLogSimpleService.list();
        return CommonResult.success(logs, "查询成功");
    }
} 