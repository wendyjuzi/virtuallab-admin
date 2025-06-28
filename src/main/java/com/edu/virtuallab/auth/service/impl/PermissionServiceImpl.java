package com.edu.virtuallab.auth.service.impl;

import com.edu.virtuallab.auth.dao.PermissionDao;
import com.edu.virtuallab.auth.model.Permission;
import com.edu.virtuallab.auth.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Override
    public boolean hasProjectAccess(Long projectId) {
        // 1. 获取当前登录用户ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = Long.parseLong(authentication.getName());

        // 2. 实现权限检查逻辑（根据业务需求）
        // 示例1: 检查用户是否是项目创建者
        // return projectDao.isProjectOwner(projectId, currentUserId);

        // 示例2: 检查用户是否有项目访问权限
        // return projectAccessService.hasAccess(projectId, currentUserId);

        // 当前返回true表示有权限（实际项目中需要替换为具体实现）
        return true;
    }
}