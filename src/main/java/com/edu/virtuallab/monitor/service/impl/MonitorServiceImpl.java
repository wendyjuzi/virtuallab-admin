package com.edu.virtuallab.monitor.service.impl;

import com.edu.virtuallab.monitor.dto.SystemStatusDTO;
import com.edu.virtuallab.monitor.mapper.ExperimentMapper;
import com.edu.virtuallab.monitor.mapper.OperationLogMapper;
import com.edu.virtuallab.monitor.service.MonitorService;
import com.edu.virtuallab.monitor.util.UserSessionCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

@Service
@RequiredArgsConstructor
public class MonitorServiceImpl implements MonitorService {

    private final UserSessionCache userSessionCache;
    private final ExperimentMapper experimentMapper;
    private final OperationLogMapper operationLogMapper;

    @Override
    public SystemStatusDTO getSystemStatus() {
        int onlineUsers = userSessionCache.getOnlineUserCount();
        int activeExperiments = experimentMapper.countActiveExperiments();
        double systemLoad = getSystemLoadAverage();
        int todayOperations = operationLogMapper.countTodayOperations();

        return new SystemStatusDTO(onlineUsers, activeExperiments, systemLoad, todayOperations);
    }

    private double getSystemLoadAverage() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        return osBean.getSystemLoadAverage();
    }
}