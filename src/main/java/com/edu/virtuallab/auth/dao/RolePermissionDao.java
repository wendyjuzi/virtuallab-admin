package com.edu.virtuallab.auth.dao;

import com.edu.virtuallab.auth.model.RolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RolePermissionDao {
    List<RolePermission> findByRoleId(Long roleId);
    int insert(RolePermission rolePermission);
    int deleteByRoleId(Long roleId);
    int deleteByPermissionId(Long permissionId);
} 