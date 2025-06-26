package com.edu.virtuallab.log.dao;

import com.edu.virtuallab.log.model.OperationLog;
import java.util.List;

public interface OperationLogDao {
    List<OperationLog> findByUserId(Long userId);
    int insert(OperationLog log);
    List<OperationLog> findAll();
} 