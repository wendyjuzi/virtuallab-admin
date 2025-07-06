package com.edu.virtuallab.log.service.impl;

import com.edu.virtuallab.log.dao.OperationLogSimpleDao;
import com.edu.virtuallab.log.model.OperationLog;
import com.edu.virtuallab.log.service.OperationLogSimpleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OperationLogSimpleServiceImpl implements OperationLogSimpleService {
    @Autowired
    private OperationLogSimpleDao operationLogSimpleDao;

    @Override
    public void add(OperationLog log) {
        operationLogSimpleDao.insert(log);
    }

    @Override
    public List<OperationLog> list() {
        return operationLogSimpleDao.selectAll();
    }
} 