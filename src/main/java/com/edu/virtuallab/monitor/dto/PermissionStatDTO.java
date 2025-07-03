package com.edu.virtuallab.monitor.dto;

import java.util.List;
import java.util.Map;

public class PermissionStatDTO {
    private int totalPermissions;
    private List<Map<String, Object>> permissionDistribution;
    private List<Map<String, Object>> rolePermissionCount;

    public int getTotalPermissions() { return totalPermissions; }
    public void setTotalPermissions(int totalPermissions) { this.totalPermissions = totalPermissions; }
    public List<Map<String, Object>> getPermissionDistribution() { return permissionDistribution; }
    public void setPermissionDistribution(List<Map<String, Object>> permissionDistribution) { this.permissionDistribution = permissionDistribution; }
    public List<Map<String, Object>> getRolePermissionCount() { return rolePermissionCount; }
    public void setRolePermissionCount(List<Map<String, Object>> rolePermissionCount) { this.rolePermissionCount = rolePermissionCount; }
} 