package com.edu.virtuallab.monitor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemStatusDTO {
    private int onlineUsers;
    private int activeExperiments;
    private double systemLoad;
    private int todayOperations;
}