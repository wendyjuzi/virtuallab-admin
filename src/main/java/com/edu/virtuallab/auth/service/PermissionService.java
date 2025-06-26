package com.edu.virtuallab.auth.service;

import com.edu.virtuallab.auth.model.Permission;
import java.util.List;

public interface PermissionService {
    Permission getById(Long id);
    Permission getByCode(String code);
    List<Permission> listAll();
    boolean create(Permission permission);
    boolean update(Permission permission);
    boolean delete(Long id);
} 