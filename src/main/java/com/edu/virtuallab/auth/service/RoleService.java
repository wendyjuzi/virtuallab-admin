package com.edu.virtuallab.auth.service;

import com.edu.virtuallab.auth.model.Role;
import java.util.List;

public interface RoleService {
    Role getById(Long id);
    Role getByName(String name);
    List<Role> listAll();
    boolean create(Role role);
    boolean update(Role role);
    boolean delete(Long id);
    boolean assignPermissions(Long roleId, List<Long> permissionIds);
} 