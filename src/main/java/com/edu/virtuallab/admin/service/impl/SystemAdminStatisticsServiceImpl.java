package com.edu.virtuallab.admin.service.impl;

import com.edu.virtuallab.admin.service.SystemAdminStatisticsService;
import com.edu.virtuallab.experiment.dao.ExperimentProjectDao;
import com.edu.virtuallab.experiment.dto.NameValueDTO;
import com.edu.virtuallab.auth.dao.UserDao;
import com.edu.virtuallab.auth.dao.DepartmentDao;
import com.edu.virtuallab.monitor.mapper.OperationLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SystemAdminStatisticsServiceImpl implements SystemAdminStatisticsService {
    @Autowired private UserDao userDao;
    @Autowired private DepartmentDao departmentDao;
    @Autowired private ExperimentProjectDao experimentProjectDao;
    @Autowired private OperationLogMapper operationLogMapper;

    @Override
    public int getTotalUsers() { return userDao.countAll(); }
    @Override
    public int getTotalDepartments() { return departmentDao.count(); }
    @Override
    public int getTotalExperiments() { return experimentProjectDao.countAll(); }
    @Override
    public int getTodayLoginCount() { return operationLogMapper.countTodayLoginOperations(); }

    @Override
    public List<NameValueDTO> getExperimentTypeDistribution() {
        return experimentProjectDao.countByType();
    }
    @Override
    public List<NameValueDTO> getExperimentActiveTrend() {
        return experimentProjectDao.countActiveByDay();
    }
    @Override
    public List<NameValueDTO> getExperimentFinishRateRank() {
        return experimentProjectDao.rankByFinishRate();
    }
    @Override
    public List<NameValueDTO> getExperimentTopParticipants() {
        return experimentProjectDao.topByParticipants();
    }

    @Override
    public java.util.List<com.edu.virtuallab.experiment.dto.NameValueDTO> getUserRoleDistribution() {
        return userDao.countUserByRole();
    }
} 