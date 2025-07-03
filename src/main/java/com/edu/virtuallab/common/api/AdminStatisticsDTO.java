package com.edu.virtuallab.common.api;

public class AdminStatisticsDTO {
    private int totalUsers;
    private int activeUsers;
    private int totalDepartments;
    private int todayLogins;

    public int getTotalUsers() {
        return totalUsers;
    }
    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }
    public int getActiveUsers() {
        return activeUsers;
    }
    public void setActiveUsers(int activeUsers) {
        this.activeUsers = activeUsers;
    }
    public int getTotalDepartments() {
        return totalDepartments;
    }
    public void setTotalDepartments(int totalDepartments) {
        this.totalDepartments = totalDepartments;
    }
    public int getTodayLogins() {
        return todayLogins;
    }
    public void setTodayLogins(int todayLogins) {
        this.todayLogins = todayLogins;
    }
} 