package com.edu.virtuallab.log.service;

import com.edu.virtuallab.log.model.OperationLog;
import java.util.List;

public interface OperationLogService {
    void log(OperationLog log);
    List<OperationLog> listByUserId(Long userId);
    List<OperationLog> listAll();
    List<OperationLog> getLatestLogs(int limit);

    OperationLog getLogById(Long id);
} 