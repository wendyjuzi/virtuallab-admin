package com.edu.virtuallab.auth.dao;

import com.edu.virtuallab.auth.model.TempPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface TempPermissionDao {
    
    // 根据用户ID查询临时权限
    List<TempPermission> findByUserId(@Param("userId") Long userId);
    
    // 查询生效中的临时权限
    List<TempPermission> findActiveByUserId(@Param("userId") Long userId, @Param("currentTime") Date currentTime);
    
    // 插入临时权限
    int insert(TempPermission tempPermission);
    
    // 更新临时权限
    int update(TempPermission tempPermission);
    
    // 删除临时权限
    int deleteById(@Param("id") Long id);
    
    // 批量删除用户的临时权限
    int deleteByUserId(@Param("userId") Long userId);
    
    // 更新过期权限状态
    int updateExpiredPermissions(@Param("currentTime") Date currentTime);
    
    // 根据角色ID查询临时权限
    List<TempPermission> findByRoleId(@Param("roleId") Long roleId);
    
    // 根据权限ID查询临时权限
    List<TempPermission> findByPermissionId(@Param("permissionId") Long permissionId);
} 