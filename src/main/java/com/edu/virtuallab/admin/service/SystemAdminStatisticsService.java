package com.edu.virtuallab.admin.service;

import com.edu.virtuallab.experiment.dto.NameValueDTO;
import java.util.List;

public interface SystemAdminStatisticsService {
    int getTotalUsers();
    int getTotalDepartments();
    int getTotalExperiments();
    int getTodayLoginCount();
    List<NameValueDTO> getExperimentTypeDistribution();
    List<NameValueDTO> getExperimentActiveTrend();
    List<NameValueDTO> getExperimentFinishRateRank();
    List<NameValueDTO> getExperimentTopParticipants();
    // 用户角色分布
    java.util.List<com.edu.virtuallab.experiment.dto.NameValueDTO> getUserRoleDistribution();
} 