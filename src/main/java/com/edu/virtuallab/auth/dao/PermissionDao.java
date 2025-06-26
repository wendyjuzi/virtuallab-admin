package com.edu.virtuallab.auth.dao;

import com.edu.virtuallab.auth.model.Permission;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionDao {
    Permission findById(Long id);
    Permission findByCode(String code);
    List<Permission> findAll();
    int insert(Permission permission);
    int update(Permission permission);
    int delete(Long id);
} 