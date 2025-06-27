package com.edu.virtuallab.auth.service;

import com.edu.virtuallab.auth.model.TempPermission;

import java.util.Date;
import java.util.List;

public interface TempPermissionService {
    
    // 根据用户ID查询临时权限
    List<TempPermission> findByUserId(Long userId);
    
    // 查询用户生效中的临时权限
    List<TempPermission> findActiveByUserId(Long userId);
    
    // 分配临时权限
    boolean grantTempPermission(TempPermission tempPermission);
    
    // 撤销临时权限
    boolean revokeTempPermission(Long id);
    
    // 批量撤销用户临时权限
    boolean revokeByUserId(Long userId);
    
    // 更新过期权限状态
    void updateExpiredPermissions();
    
    // 根据角色ID查询临时权限
    List<TempPermission> findByRoleId(Long roleId);
    
    // 根据权限ID查询临时权限
    List<TempPermission> findByPermissionId(Long permissionId);
    
    // 检查用户是否有临时权限
    boolean hasTempPermission(Long userId, Long permissionId);
    
    // 检查用户是否有临时角色
    boolean hasTempRole(Long userId, Long roleId);
    
    // 获取用户所有生效的临时权限ID列表
    List<Long> getActivePermissionIds(Long userId);
    
    // 获取用户所有生效的临时角色ID列表
    List<Long> getActiveRoleIds(Long userId);
} 