package com.edu.virtuallab.admin.controller;

import com.edu.virtuallab.admin.service.SystemAdminStatisticsService;
import com.edu.virtuallab.experiment.dto.NameValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system-admin/statistics")
public class SystemAdminStatisticsController {
    @Autowired
    private SystemAdminStatisticsService statisticsService;

    @GetMapping("/overview")
    public Map<String, Object> getOverview() {
        Map<String, Object> map = new HashMap<>();
        map.put("totalUsers", statisticsService.getTotalUsers());
        map.put("totalDepartments", statisticsService.getTotalDepartments());
        map.put("totalExperiments", statisticsService.getTotalExperiments());
        map.put("todayLogins", statisticsService.getTodayLoginCount());
        return Map.of("data", map);
    }

    @GetMapping("/experiment-type")
    public List<NameValueDTO> getExperimentTypeDistribution() {
        return statisticsService.getExperimentTypeDistribution();
    }

    @GetMapping("/experiment-active")
    public List<NameValueDTO> getExperimentActiveTrend() {
        return statisticsService.getExperimentActiveTrend();
    }

    @GetMapping("/experiment-finish-rate")
    public List<NameValueDTO> getExperimentFinishRateRank() {
        return statisticsService.getExperimentFinishRateRank();
    }

    @GetMapping("/experiment-top-participants")
    public List<NameValueDTO> getExperimentTopParticipants() {
        return statisticsService.getExperimentTopParticipants();
    }

    @GetMapping("/user-role-distribution")
    public java.util.List<com.edu.virtuallab.experiment.dto.NameValueDTO> getUserRoleDistribution() {
        return statisticsService.getUserRoleDistribution();
    }
} 