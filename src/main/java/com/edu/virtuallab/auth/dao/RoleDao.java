package com.edu.virtuallab.auth.dao;

import com.edu.virtuallab.auth.model.Role;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleDao {
    Role findById(Long id);
    Role findByName(String name);
    List<Role> findAll();
    int insert(Role role);
    int update(Role role);
    int delete(Long id);
} 