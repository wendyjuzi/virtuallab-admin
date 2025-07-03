package com.edu.virtuallab.auth.service;

import com.edu.virtuallab.auth.dto.UserCreateDTO;
import com.edu.virtuallab.auth.dto.PermissionTemplateDTO;
import com.edu.virtuallab.auth.model.*;
import com.edu.virtuallab.common.api.PageResult;
import com.edu.virtuallab.log.model.OperationLog;
import com.edu.virtuallab.common.api.StatisticsDTO;
import com.edu.virtuallab.common.api.AdminStatisticsDTO;

import java.util.List;

/**
 * 系统管理员服务接口
 * 负责系统级别的权限管理功能
 */
public interface SystemAdminService {
    
    // ==================== 账号全生命周期管理 ====================
    
    /**
     * 创建用户账号（支持所有角色）
     */
    User createUser(UserCreateDTO userCreateDTO);
    
    /**
     * 批量创建用户账号
     */
    List<User> batchCreateUsers(List<UserCreateDTO> userCreateDTOs);
    
    /**
     * 修改用户信息
     */
    boolean updateUser(User user);
    
    /**
     * 停用用户账号
     */
    boolean disableUser(Long userId, String reason);
    
    /**
     * 启用用户账号
     */
    boolean enableUser(Long userId);
    
    /**
     * 删除用户账号
     */
    boolean deleteUser(Long userId);
    
    /**
     * 重置用户密码
     */
    boolean resetUserPassword(Long userId, String newPassword);
    
    /**
     * 锁定用户账号
     */
    boolean lockUser(Long userId, String reason);
    
    /**
     * 解锁用户账号
     */
    boolean unlockUser(Long userId);
    
    // ==================== 角色权限全局管理 ====================
    
    /**
     * 创建角色
     */
    Role createRole(Role role);
    
    /**
     * 更新角色
     */
    boolean updateRole(Role role);
    
    /**
     * 删除角色
     */
    boolean deleteRole(Long roleId);
    
    /**
     * 为角色分配权限
     */
    boolean assignPermissionsToRole(Long roleId, List<Long> permissionIds);
    
    /**
     * 创建权限模板
     */
    boolean createPermissionTemplate(PermissionTemplateDTO template);
    
    /**
     * 更新权限模板
     */
    boolean updatePermissionTemplate(PermissionTemplateDTO template);
    
    /**
     * 删除权限模板
     */
    boolean deletePermissionTemplate(Long templateId);
    
    /**
     * 应用权限模板到角色
     */
    boolean applyPermissionTemplate(Long templateId, Long roleId);
    
    /**
     * 为用户分配角色
     */
    boolean assignRolesToUser(Long userId, List<Long> roleIds);
    
    /**
     * 移除用户角色
     */
    boolean removeUserRoles(Long userId, List<Long> roleIds);
    
    /**
     * 设置临时权限
     */
    boolean setTemporaryPermission(Long userId, Long roleId, String startTime, String endTime, String reason);
    
    /**
     * 回收临时权限
     */
    boolean revokeTemporaryPermission(Long userRoleId);
    
    // ==================== 院系管理员管理 ====================
    
    /**
     * 创建院系管理员
     */
    User createDepartmentAdmin(UserCreateDTO userCreateDTO);
    
    /**
     * 为院系管理员分配权限
     */
    boolean assignDepartmentAdminPermissions(Long userId, Long departmentId, List<Long> permissionIds);
    
    /**
     * 调整院系管理员权限范围
     */
    boolean adjustDepartmentAdminPermissions(Long userId, List<Long> permissionIds);
    
    /**
     * 停用院系管理员
     */
    boolean disableDepartmentAdmin(Long userId);
    
    // ==================== 院系管理 ====================
    
    /**
     * 创建院系
     */
    Department createDepartment(Department department);
    
    /**
     * 更新院系信息
     */
    boolean updateDepartment(Department department);
    
    /**
     * 删除院系
     */
    boolean deleteDepartment(Long departmentId);
    
    /**
     * 获取院系列表
     */
    List<Department> getDepartmentList();
    
    // ==================== 系统监控与审计 ====================
    
    /**
     * 获取操作日志
     */
    PageResult<OperationLog> getOperationLogs(String username, String operation, String module, 
                                            String startTime, String endTime, int page, int size);
    
    /**
     * 导出操作日志
     */
    String exportOperationLogs(String username, String operation, String module, 
                              String startTime, String endTime);
    
    /**
     * 获取用户登录统计
     */
    List<Object> getUserLoginStatistics(String startTime, String endTime);
    
    /**
     * 获取权限使用统计
     */
    List<Object> getPermissionUsageStatistics(String startTime, String endTime);
    
    /**
     * 生成安全审计报告
     */
    String generateSecurityAuditReport(String startTime, String endTime);
    
    // ==================== 查询功能 ====================
    
    /**
     * 分页查询用户列表
     */
    PageResult<User> getUserList(String username, String realName, String department, 
                                String userType, Integer status, int page, int size);
    
    /**
     * 根据ID获取用户信息
     */
    User getUserById(Long id);
    
    /**
     * 获取角色列表
     */
    List<Role> getRoleList();
    
    /**
     * 获取权限列表
     */
    List<Permission> getPermissionList();
    
    /**
     * 获取用户角色
     */
    List<Role> getUserRoles(Long userId);
    
    /**
     * 获取角色权限
     */
    List<Permission> getRolePermissions(Long roleId);
    
    // ==================== 统计功能 ====================
    
    /**
     * 获取活跃用户数量（最近7天登录的用户）
     */
    int getActiveUserCount();
    
    /**
     * 获取今日登录数量
     */
    int getTodayLoginCount();
    
    AdminStatisticsDTO getStatistics();
} 