package com.edu.virtuallab.monitor.dto;

import java.util.List;
import java.util.Map;

public class UserBehaviorDTO {
    private int activeUserCount;
    private List<Map<String, Object>> operationTypeStats;
    private List<Map<String, Object>> dailyActiveUsers;
    private java.util.List<String> timePoints;
    private java.util.List<Integer> viewData;
    private java.util.List<Integer> createData;
    private java.util.List<Integer> editData;
    private java.util.List<Integer> deleteData;

    public int getActiveUserCount() { return activeUserCount; }
    public void setActiveUserCount(int activeUserCount) { this.activeUserCount = activeUserCount; }
    public List<Map<String, Object>> getOperationTypeStats() { return operationTypeStats; }
    public void setOperationTypeStats(List<Map<String, Object>> operationTypeStats) { this.operationTypeStats = operationTypeStats; }
    public List<Map<String, Object>> getDailyActiveUsers() { return dailyActiveUsers; }
    public void setDailyActiveUsers(List<Map<String, Object>> dailyActiveUsers) { this.dailyActiveUsers = dailyActiveUsers; }
    public java.util.List<String> getTimePoints() { return timePoints; }
    public void setTimePoints(java.util.List<String> timePoints) { this.timePoints = timePoints; }
    public java.util.List<Integer> getViewData() { return viewData; }
    public void setViewData(java.util.List<Integer> viewData) { this.viewData = viewData; }
    public java.util.List<Integer> getCreateData() { return createData; }
    public void setCreateData(java.util.List<Integer> createData) { this.createData = createData; }
    public java.util.List<Integer> getEditData() { return editData; }
    public void setEditData(java.util.List<Integer> editData) { this.editData = editData; }
    public java.util.List<Integer> getDeleteData() { return deleteData; }
    public void setDeleteData(java.util.List<Integer> deleteData) { this.deleteData = deleteData; }
} 