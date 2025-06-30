package com.edu.virtuallab.auth.service.impl;

import com.edu.virtuallab.auth.dao.RoleDao;
import com.edu.virtuallab.auth.dao.RolePermissionDao;
import com.edu.virtuallab.auth.model.Role;
import com.edu.virtuallab.auth.model.RolePermission;
import com.edu.virtuallab.auth.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Override
    public Role getById(Long id) {
        return roleDao.findById(id);
    }

    @Override
    public Role getByName(String name) {
        return roleDao.findByName(name);
    }

    @Override
    public List<Role> listAll() {
        return roleDao.findAll();
    }

    @Override
    public boolean create(Role role) {
        return roleDao.insert(role) > 0;
    }

    @Override
    public boolean update(Role role) {
        return roleDao.update(role) > 0;
    }

    @Override
    public boolean delete(Long id) {
        rolePermissionDao.deleteByRoleId(id);
        return roleDao.delete(id) > 0;
    }

    @Override
    public boolean assignPermissions(Long roleId, List<Long> permissionIds) {
        rolePermissionDao.deleteByRoleId(roleId);
        boolean success = true;
        for (Long permissionId : permissionIds) {
            RolePermission rp = new RolePermission();
            rp.setRoleId(roleId);
            rp.setPermissionId(permissionId);
            success &= rolePermissionDao.insert(rp) > 0;
        }
        return success;
    }

    @Override
    public List<Role> getRolesByUserId(Long userId) {
        return roleDao.findByUserId(userId);
    }
} 