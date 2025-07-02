package com.edu.virtuallab.log.service.impl;

import com.edu.virtuallab.log.dao.OperationLogDao;
import com.edu.virtuallab.log.model.OperationLog;
import com.edu.virtuallab.log.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationLogServiceImpl implements OperationLogService {
    @Autowired
    private OperationLogDao operationLogDao;

    @Override
    public void log(OperationLog log) {
        operationLogDao.insert(log);
    }

    @Override
    public List<OperationLog> listByUserId(Long userId) {
        return operationLogDao.findByUserId(userId);
    }

    @Override
    public List<OperationLog> listAll() {
        return operationLogDao.findAll();
    }

    @Override
    public List<OperationLog> getLatestLogs(int limit) {
        return operationLogDao.selectLatestLogs(limit);
    }

    @Override
    public OperationLog getLogById(Long id) {
        return operationLogDao.findById(id);
    }
} 