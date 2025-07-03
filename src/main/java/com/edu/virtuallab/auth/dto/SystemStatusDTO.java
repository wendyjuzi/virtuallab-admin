package com.edu.virtuallab.auth.dto;

import lombok.Data;

@Data
public class SystemStatusDTO {
    private int onlineUsers;         // 在线用户数
    private int activeExperiments;   // 活跃实验数
    private double systemLoad;       // 系统负载（CPU负载）
    private int todayOperations;     // 今日操作数
}