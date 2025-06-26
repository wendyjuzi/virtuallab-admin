package com.edu.virtuallab.auth.service.impl;

import com.edu.virtuallab.auth.dao.PermissionDao;
import com.edu.virtuallab.auth.model.Permission;
import com.edu.virtuallab.auth.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;

    @Override
    public Permission getById(Long id) {
        return permissionDao.findById(id);
    }

    @Override
    public Permission getByCode(String code) {
        return permissionDao.findByCode(code);
    }

    @Override
    public List<Permission> listAll() {
        return permissionDao.findAll();
    }

    @Override
    public boolean create(Permission permission) {
        return permissionDao.insert(permission) > 0;
    }

    @Override
    public boolean update(Permission permission) {
        return permissionDao.update(permission) > 0;
    }

    @Override
    public boolean delete(Long id) {
        return permissionDao.delete(id) > 0;
    }
} 