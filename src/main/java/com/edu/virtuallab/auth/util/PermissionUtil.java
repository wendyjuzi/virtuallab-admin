package com.edu.virtuallab.auth.util;

import com.edu.virtuallab.auth.model.TempPermission;
import com.edu.virtuallab.auth.service.TempPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限工具类
 * 提供权限检查、验证等实用方法
 */
@Component
public class PermissionUtil {
    
    @Autowired
    private TempPermissionService tempPermissionService;
    
    /**
     * 检查用户是否有指定权限（包括临时权限）
     * @param userId 用户ID
     * @param permissionId 权限ID
     * @param hasPermanentPermission 是否有永久权限
     * @return 是否有权限
     */
    public boolean hasPermission(Long userId, Long permissionId, boolean hasPermanentPermission) {
        // 如果有永久权限，直接返回true
        if (hasPermanentPermission) {
            return true;
        }
        
        // 检查临时权限
        return tempPermissionService.hasTempPermission(userId, permissionId);
    }
    
    /**
     * 检查用户是否有指定角色（包括临时角色）
     * @param userId 用户ID
     * @param roleId 角色ID
     * @param hasPermanentRole 是否有永久角色
     * @return 是否有角色
     */
    public boolean hasRole(Long userId, Long roleId, boolean hasPermanentRole) {
        // 如果有永久角色，直接返回true
        if (hasPermanentRole) {
            return true;
        }
        
        // 检查临时角色
        return tempPermissionService.hasTempRole(userId, roleId);
    }
    
    /**
     * 获取用户所有权限ID（包括永久权限和临时权限）
     * @param userId 用户ID
     * @param permanentPermissionIds 永久权限ID列表
     * @return 所有权限ID列表
     */
    public List<Long> getAllPermissionIds(Long userId, List<Long> permanentPermissionIds) {
        List<Long> tempPermissionIds = tempPermissionService.getActivePermissionIds(userId);
        
        // 合并永久权限和临时权限，去重
        tempPermissionIds.addAll(permanentPermissionIds);
        return tempPermissionIds.stream().distinct().collect(Collectors.toList());
    }
    
    /**
     * 获取用户所有角色ID（包括永久角色和临时角色）
     * @param userId 用户ID
     * @param permanentRoleIds 永久角色ID列表
     * @return 所有角色ID列表
     */
    public List<Long> getAllRoleIds(Long userId, List<Long> permanentRoleIds) {
        List<Long> tempRoleIds = tempPermissionService.getActiveRoleIds(userId);
        
        // 合并永久角色和临时角色，去重
        tempRoleIds.addAll(permanentRoleIds);
        return tempRoleIds.stream().distinct().collect(Collectors.toList());
    }
    
    /**
     * 验证临时权限时间范围
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 是否有效
     */
    public boolean validateTimeRange(Date startTime, Date endTime) {
        if (startTime == null || endTime == null) {
            return false;
        }
        
        // 开始时间不能晚于结束时间
        if (startTime.after(endTime)) {
            return false;
        }
        
        // 结束时间不能早于当前时间（如果开始时间已经过去）
        Date now = new Date();
        if (startTime.before(now) && endTime.before(now)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * 检查临时权限是否即将过期（7天内）
     * @param tempPermission 临时权限
     * @return 是否即将过期
     */
    public boolean isExpiringSoon(TempPermission tempPermission) {
        if (tempPermission == null || tempPermission.getEndTime() == null) {
            return false;
        }
        
        Date now = new Date();
        Date endTime = tempPermission.getEndTime();
        
        // 计算剩余天数
        long diffInMillies = endTime.getTime() - now.getTime();
        long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);
        
        return diffInDays <= 7 && diffInDays >= 0;
    }
    
    /**
     * 检查临时权限是否已过期
     * @param tempPermission 临时权限
     * @return 是否已过期
     */
    public boolean isExpired(TempPermission tempPermission) {
        if (tempPermission == null || tempPermission.getEndTime() == null) {
            return false;
        }
        
        Date now = new Date();
        return tempPermission.getEndTime().before(now);
    }
    
    /**
     * 检查临时权限是否已生效
     * @param tempPermission 临时权限
     * @return 是否已生效
     */
    public boolean isActive(TempPermission tempPermission) {
        if (tempPermission == null) {
            return false;
        }
        
        Date now = new Date();
        
        // 检查开始时间和结束时间
        if (tempPermission.getStartTime() != null && tempPermission.getStartTime().after(now)) {
            return false; // 还未开始
        }
        
        if (tempPermission.getEndTime() != null && tempPermission.getEndTime().before(now)) {
            return false; // 已过期
        }
        
        return true;
    }
    
    /**
     * 获取临时权限剩余天数
     * @param tempPermission 临时权限
     * @return 剩余天数，-1表示已过期
     */
    public long getRemainingDays(TempPermission tempPermission) {
        if (tempPermission == null || tempPermission.getEndTime() == null) {
            return -1;
        }
        
        Date now = new Date();
        Date endTime = tempPermission.getEndTime();
        
        if (endTime.before(now)) {
            return -1; // 已过期
        }
        
        long diffInMillies = endTime.getTime() - now.getTime();
        return diffInMillies / (24 * 60 * 60 * 1000);
    }
    
    /**
     * 格式化权限状态描述
     * @param status 状态码
     * @return 状态描述
     */
    public String formatStatus(Integer status) {
        if (status == null) {
            return "未知";
        }
        
        switch (status) {
            case TempPermission.STATUS_PENDING:
                return "待生效";
            case TempPermission.STATUS_ACTIVE:
                return "生效中";
            case TempPermission.STATUS_EXPIRED:
                return "已失效";
            default:
                return "未知";
        }
    }
    
    /**
     * 检查权限分配是否重复
     * @param userId 用户ID
     * @param permissionId 权限ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 是否重复
     */
    public boolean isDuplicatePermission(Long userId, Long permissionId, Date startTime, Date endTime) {
        List<TempPermission> existingPermissions = tempPermissionService.findByUserId(userId);
        
        return existingPermissions.stream()
                .anyMatch(tp -> tp.getPermissionId().equals(permissionId) &&
                        hasTimeOverlap(tp.getStartTime(), tp.getEndTime(), startTime, endTime));
    }
    
    /**
     * 检查角色分配是否重复
     * @param userId 用户ID
     * @param roleId 角色ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 是否重复
     */
    public boolean isDuplicateRole(Long userId, Long roleId, Date startTime, Date endTime) {
        List<TempPermission> existingPermissions = tempPermissionService.findByUserId(userId);
        
        return existingPermissions.stream()
                .anyMatch(tp -> tp.getRoleId().equals(roleId) &&
                        hasTimeOverlap(tp.getStartTime(), tp.getEndTime(), startTime, endTime));
    }
    
    /**
     * 检查时间范围是否重叠
     * @param start1 第一个开始时间
     * @param end1 第一个结束时间
     * @param start2 第二个开始时间
     * @param end2 第二个结束时间
     * @return 是否重叠
     */
    private boolean hasTimeOverlap(Date start1, Date end1, Date start2, Date end2) {
        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            return false;
        }
        
        // 检查是否有重叠：第一个时间段的开始时间早于第二个时间段的结束时间，
        // 且第一个时间段的结束时间晚于第二个时间段的开始时间
        return start1.before(end2) && end1.after(start2);
    }
} 