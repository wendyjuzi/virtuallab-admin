package com.edu.virtuallab.auth.service;

import com.edu.virtuallab.auth.dto.UserCreateDTO;
import com.edu.virtuallab.auth.model.*;
import com.edu.virtuallab.common.api.PageResult;
import com.edu.virtuallab.log.model.OperationLog;

import java.util.List;

/**
 * 院系管理员服务接口
 * 负责本院系范围内的权限管理功能
 */
public interface DepartmentAdminService {
    
    // ==================== 本院系账号管理 ====================
    
    /**
     * 创建本院系用户账号（教师、学生、访客）
     */
    User createDepartmentUser(UserCreateDTO userCreateDTO, Long adminUserId);
    
    /**
     * 批量创建本院系用户账号
     */
    List<User> batchCreateDepartmentUsers(List<UserCreateDTO> userCreateDTOs, Long adminUserId);
    
    /**
     * 修改本院系用户信息
     */
    boolean updateDepartmentUser(User user, Long adminUserId);
    
    /**
     * 停用本院系用户账号
     */
    boolean disableDepartmentUser(Long userId, String reason, Long adminUserId);
    
    /**
     * 启用本院系用户账号
     */
    boolean enableDepartmentUser(Long userId, Long adminUserId);
    
    /**
     * 重置本院系用户密码
     */
    boolean resetDepartmentUserPassword(Long userId, String newPassword, Long adminUserId);
    
    /**
     * 锁定本院系用户账号
     */
    boolean lockDepartmentUser(Long userId, String reason, Long adminUserId);
    
    /**
     * 解锁本院系用户账号
     */
    boolean unlockDepartmentUser(Long userId, Long adminUserId);
    
    // ==================== 本院系权限管理 ====================
    
    /**
     * 为本院系用户分配角色
     */
    boolean assignRolesToDepartmentUser(Long userId, List<Long> roleIds, Long adminUserId);
    
    /**
     * 移除本院系用户角色
     */
    boolean removeDepartmentUserRoles(Long userId, List<Long> roleIds, Long adminUserId);
    
    /**
     * 为本院系用户设置临时权限
     */
    boolean setDepartmentUserTemporaryPermission(Long userId, Long roleId, String startTime, 
                                                String endTime, String reason, Long adminUserId);
    
    /**
     * 回收本院系用户临时权限
     */
    boolean revokeDepartmentUserTemporaryPermission(Long userRoleId, Long adminUserId);
    
    /**
     * 调整本院系用户权限（在系统管理员设定的模板基础上微调）
     */
    boolean adjustDepartmentUserPermissions(Long userId, List<Long> permissionIds, Long adminUserId);
    
    // ==================== 教学资源与数据管理 ====================
    
    /**
     * 管理本院系教学资源
     */
    boolean manageDepartmentResource(Long resourceId, String operation, Long adminUserId);
    
    /**
     * 审核本院系教师提交的资源
     */
    boolean approveDepartmentResource(Long resourceId, String approvalResult, String comment, Long adminUserId);
    
    /**
     * 管理本院系学生成绩数据
     */
    boolean manageDepartmentScore(Long scoreId, String operation, Long adminUserId);
    
    /**
     * 生成本院系成绩报表
     */
    String generateDepartmentScoreReport(Long adminUserId);
    
    // ==================== 与系统管理员协同 ====================
    
    /**
     * 提交权限申请
     */
    boolean submitPermissionRequest(String requestType, String description, List<Long> userIds, 
                                   List<Long> permissionIds, Long adminUserId);
    
    /**
     * 上报账号异常
     */
    boolean reportUserAbnormal(Long userId, String abnormalType, String description, Long adminUserId);
    
    /**
     * 获取权限申请状态
     */
    List<Object> getPermissionRequestStatus(Long adminUserId);
    
    // ==================== 查询功能 ====================
    
    /**
     * 分页查询本院系用户列表
     */
    PageResult<User> getDepartmentUserList(String username, String realName, String userType, 
                                          Integer status, Long adminUserId, int page, int size);
    
    /**
     * 获取本院系用户角色
     */
    List<Role> getDepartmentUserRoles(Long userId, Long adminUserId);
    
    /**
     * 获取本院系可分配的角色列表
     */
    List<Role> getDepartmentAssignableRoles(Long adminUserId);
    
    /**
     * 获取本院系可分配的权限列表
     */
    List<Permission> getDepartmentAssignablePermissions(Long adminUserId);
    
    /**
     * 获取本院系操作日志
     */
    PageResult<OperationLog> getDepartmentOperationLogs(String username, String operation, 
                                                       String module, String startTime, 
                                                       String endTime, Long adminUserId, 
                                                       int page, int size);
    
    /**
     * 获取本院系用户统计
     */
    Object getDepartmentUserStatistics(Long adminUserId);
    
    /**
     * 获取本院系权限使用统计
     */
    Object getDepartmentPermissionUsageStatistics(Long adminUserId);
    /**
     * 获取本院系学生数量
     */
    int countDepartmentStudents(Long adminUserId);
    /**
     * 获取本院系老师数量
     */
    int countDepartmentTeachers(Long adminUserId);
}