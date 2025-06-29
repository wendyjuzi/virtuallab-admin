package com.edu.virtuallab.auth.dao;

import com.edu.virtuallab.auth.model.Permission;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PermissionDao {
    Permission findById(Long id);
    Permission findByCode(String code);
    List<Permission> findAll();
    int insert(Permission permission);
    int update(Permission permission);
    int delete(Long id);
    List<Permission> findByRoleId(Long roleId);
    List<Permission> findByDepartmentId(Long departmentId);

    /**
     * 根据院系ID统计权限使用情况
     */
    Object countPermissionUsageByDepartment(@Param("departmentId") Long departmentId);

    void clearUserPermissions(Long userId);

    void assignPermissionsToUser(Long userId, List<Long> permissionIds);
} 