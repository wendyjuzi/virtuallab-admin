package com.edu.virtuallab.auth.dao;

import com.edu.virtuallab.auth.model.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserRoleDao {
    List<UserRole> findByUserId(Long userId);
    int insert(UserRole userRole);
    int deleteByUserId(Long userId);
    int deleteByRoleId(Long roleId);
    List<UserRole> findByRoleId(Long roleId);
    int deleteByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);
    int deleteById(Long id);
    int deleteByUserIdAndDepartmentId(@Param("userId") Long userId, @Param("departmentId") Long departmentId);
    int deleteByUserIdAndRoleIdAndDepartmentId(@Param("userId") Long userId, @Param("roleId") Long roleId, @Param("departmentId") Long departmentId);
    List<UserRole> findByUserIdAndDepartmentId(@Param("userId") Long userId, @Param("departmentId") Long departmentId);
} 