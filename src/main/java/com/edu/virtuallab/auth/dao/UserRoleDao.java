package com.edu.virtuallab.auth.dao;

import com.edu.virtuallab.auth.model.UserRole;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleDao {
    List<UserRole> findByUserId(Long userId);
    int insert(UserRole userRole);
    int deleteByUserId(Long userId);
    int deleteByRoleId(Long roleId);
} 