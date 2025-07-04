package com.edu.virtuallab.monitor.dto;

import java.util.List;
import java.util.Map;

public class UserBehaviorDTO {
    private int activeUserCount;
    private List<Map<String, Object>> operationTypeStats;
    private List<Map<String, Object>> dailyActiveUsers;

    public int getActiveUserCount() { return activeUserCount; }
    public void setActiveUserCount(int activeUserCount) { this.activeUserCount = activeUserCount; }
    public List<Map<String, Object>> getOperationTypeStats() { return operationTypeStats; }
    public void setOperationTypeStats(List<Map<String, Object>> operationTypeStats) { this.operationTypeStats = operationTypeStats; }
    public List<Map<String, Object>> getDailyActiveUsers() { return dailyActiveUsers; }
    public void setDailyActiveUsers(List<Map<String, Object>> dailyActiveUsers) { this.dailyActiveUsers = dailyActiveUsers; }
} 