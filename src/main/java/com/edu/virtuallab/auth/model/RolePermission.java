package com.edu.virtuallab.auth.model;

public class RolePermission {
    private Long roleId;
    private Long permissionId;
    // getter/setter
    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
    public Long getPermissionId() { return permissionId; }
    public void setPermissionId(Long permissionId) { this.permissionId = permissionId; }
} 