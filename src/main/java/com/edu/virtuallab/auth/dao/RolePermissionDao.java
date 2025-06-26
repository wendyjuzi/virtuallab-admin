package com.edu.virtuallab.auth.dao;

import com.edu.virtuallab.auth.model.RolePermission;
import java.util.List;

public interface RolePermissionDao {
    List<RolePermission> findByRoleId(Long roleId);
    int insert(RolePermission rolePermission);
    int deleteByRoleId(Long roleId);
    int deleteByPermissionId(Long permissionId);
} 