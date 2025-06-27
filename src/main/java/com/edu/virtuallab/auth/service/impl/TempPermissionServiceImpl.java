package com.edu.virtuallab.auth.service.impl;

import com.edu.virtuallab.auth.dao.TempPermissionDao;
import com.edu.virtuallab.auth.model.TempPermission;
import com.edu.virtuallab.auth.service.TempPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TempPermissionServiceImpl implements TempPermissionService {
    
    @Autowired
    private TempPermissionDao tempPermissionDao;
    
    @Override
    public List<TempPermission> findByUserId(Long userId) {
        return tempPermissionDao.findByUserId(userId);
    }
    
    @Override
    public List<TempPermission> findActiveByUserId(Long userId) {
        return tempPermissionDao.findActiveByUserId(userId, new Date());
    }
    
    @Override
    public boolean grantTempPermission(TempPermission tempPermission) {
        tempPermission.setCreateTime(new Date());
        tempPermission.setUpdateTime(new Date());
        
        // 如果开始时间为空，设置为当前时间
        if (tempPermission.getStartTime() == null) {
            tempPermission.setStartTime(new Date());
        }
        
        // 设置初始状态
        if (tempPermission.getStatus() == null) {
            if (tempPermission.getStartTime().after(new Date())) {
                tempPermission.setStatus(TempPermission.STATUS_PENDING);
            } else {
                tempPermission.setStatus(TempPermission.STATUS_ACTIVE);
            }
        }
        
        return tempPermissionDao.insert(tempPermission) > 0;
    }
    
    @Override
    public boolean revokeTempPermission(Long id) {
        return tempPermissionDao.deleteById(id) > 0;
    }
    
    @Override
    public boolean revokeByUserId(Long userId) {
        return tempPermissionDao.deleteByUserId(userId) > 0;
    }
    
    @Override
    public void updateExpiredPermissions() {
        tempPermissionDao.updateExpiredPermissions(new Date());
    }
    
    @Override
    public List<TempPermission> findByRoleId(Long roleId) {
        return tempPermissionDao.findByRoleId(roleId);
    }
    
    @Override
    public List<TempPermission> findByPermissionId(Long permissionId) {
        return tempPermissionDao.findByPermissionId(permissionId);
    }
    
    @Override
    public boolean hasTempPermission(Long userId, Long permissionId) {
        List<TempPermission> tempPermissions = findActiveByUserId(userId);
        return tempPermissions.stream()
                .anyMatch(tp -> tp.getPermissionId().equals(permissionId) && 
                        tp.getStatus().equals(TempPermission.STATUS_ACTIVE));
    }
    
    @Override
    public boolean hasTempRole(Long userId, Long roleId) {
        List<TempPermission> tempPermissions = findActiveByUserId(userId);
        return tempPermissions.stream()
                .anyMatch(tp -> tp.getRoleId().equals(roleId) && 
                        tp.getStatus().equals(TempPermission.STATUS_ACTIVE));
    }
    
    @Override
    public List<Long> getActivePermissionIds(Long userId) {
        List<TempPermission> tempPermissions = findActiveByUserId(userId);
        return tempPermissions.stream()
                .filter(tp -> tp.getPermissionId() != null)
                .map(TempPermission::getPermissionId)
                .distinct()
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Long> getActiveRoleIds(Long userId) {
        List<TempPermission> tempPermissions = findActiveByUserId(userId);
        return tempPermissions.stream()
                .filter(tp -> tp.getRoleId() != null)
                .map(TempPermission::getRoleId)
                .distinct()
                .collect(Collectors.toList());
    }
} 