package com.edu.virtuallab.monitor.service;

import com.edu.virtuallab.monitor.dto.SystemStatusDTO;
import com.edu.virtuallab.monitor.dto.SystemPerformanceDTO;
import com.edu.virtuallab.monitor.dto.UserBehaviorDTO;
import java.util.Date;

public interface MonitorService {
    SystemStatusDTO getSystemStatus();
    SystemPerformanceDTO getSystemPerformance();
    UserBehaviorDTO getUserBehaviorAnalysis(Date startTime, Date endTime);
    int getTodayLoginOperationCount();
    int getTodayLoginUserCount();
}