package com.edu.virtuallab.log.service;

import com.edu.virtuallab.log.model.OperationLog;
import com.edu.virtuallab.notification.model.PageResult;

import java.util.List;

public interface OperationLogService {
    void log(OperationLog log);
    List<OperationLog> listByUserId(Long userId);
    List<OperationLog> listAll();
    List<OperationLog> getLatestLogs(int limit);
    PageResult<OperationLog> queryLogs(int page, int size, String keyword, String type);

    OperationLog getLogById(Long id);


}