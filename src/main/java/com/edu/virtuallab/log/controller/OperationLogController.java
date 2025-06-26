package com.edu.virtuallab.log.controller;

import com.edu.virtuallab.log.model.OperationLog;
import com.edu.virtuallab.log.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@RestController
@RequestMapping("/api/log")
public class OperationLogController {
    @Autowired
    private OperationLogService logService;

    @PostMapping("/add")
    public void log(@RequestBody OperationLog log) {
        logService.log(log);
    }

    @GetMapping("/user/{userId}")
    public List<OperationLog> listByUserId(@PathVariable Long userId) {
        return logService.listByUserId(userId);
    }

    @GetMapping("/list")
    public List<OperationLog> listAll() {
        return logService.listAll();
    }
} 