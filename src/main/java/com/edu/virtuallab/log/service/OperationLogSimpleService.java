package com.edu.virtuallab.log.service;

import com.edu.virtuallab.log.model.OperationLog;
import java.util.List;

public interface OperationLogSimpleService {
    void add(OperationLog log);
    List<OperationLog> list();
} 