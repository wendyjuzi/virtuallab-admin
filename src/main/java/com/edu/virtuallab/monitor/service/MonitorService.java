package com.edu.virtuallab.monitor.service;

import com.edu.virtuallab.monitor.dto.SystemStatusDTO;

public interface MonitorService {
    SystemStatusDTO getSystemStatus();
}