package com.edu.virtuallab.log.service.impl;

import com.edu.virtuallab.log.dao.OperationLogDao;
import com.edu.virtuallab.log.model.OperationLog;
import com.edu.virtuallab.log.service.OperationLogService;
import com.edu.virtuallab.notification.model.PageResult;
import com.github.pagehelper.PageHelper;
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
    public PageResult<OperationLog> queryLogs(int page, int size, String keyword, String type) {
        PageHelper.startPage(page, size);
        List<OperationLog> logs = operationLogDao.selectByConditions(keyword, type);
        return PageResult.build(logs);
    }

    @Override
    public List<OperationLog> queryLogs(String username, String operation, String module, Integer status, String startTime, String endTime) {
        return operationLogDao.selectLogs(username, operation, module, status, startTime, endTime);
    }

    @Override
    public OperationLog getLogById(Long id) {
        return operationLogDao.findById(id);
    }
}
