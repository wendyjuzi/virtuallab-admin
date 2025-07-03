package com.edu.virtuallab.auth.service.impl;

import com.edu.virtuallab.auth.dao.*;
import com.edu.virtuallab.auth.dto.UserCreateDTO;
import com.edu.virtuallab.auth.dto.PermissionTemplateDTO;
import com.edu.virtuallab.auth.model.*;
import com.edu.virtuallab.auth.service.*;
import com.edu.virtuallab.common.api.PageResult;
import com.edu.virtuallab.common.api.StatisticsDTO;
import com.edu.virtuallab.common.api.AdminStatisticsDTO;
import com.edu.virtuallab.common.exception.BusinessException;
import com.edu.virtuallab.log.dao.OperationLogDao;
import com.edu.virtuallab.log.model.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统管理员服务实现类
 */
@Service
public class SystemAdminServiceImpl implements SystemAdminService {
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private RoleDao roleDao;
    
    @Autowired
    private PermissionDao permissionDao;
    
    @Autowired
    private UserRoleDao userRoleDao;
    
    @Autowired
    private RolePermissionDao rolePermissionDao;
    
    @Autowired
    private DepartmentDao departmentDao;
    
    @Autowired
    private OperationLogDao operationLogDao;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // ==================== 账号全生命周期管理 ====================
    
    @Override
    @Transactional
    public User createUser(UserCreateDTO userCreateDTO) {
        // 检查用户名是否已存在
        User existingUser = userDao.findByUsername(userCreateDTO.getUsername());
        if (existingUser != null) {
            throw new BusinessException("用户名已存在");
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(userCreateDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        user.setPhone(userCreateDTO.getPhone());
        user.setEmail(userCreateDTO.getEmail());
        user.setRealName(userCreateDTO.getRealName());
        user.setStudentId(userCreateDTO.getStudentId());
        user.setDepartment(userCreateDTO.getDepartment());
        user.setMajor(userCreateDTO.getMajor());
        user.setGrade(userCreateDTO.getGrade());
        user.setClassName(userCreateDTO.getClassName());
        user.setStatus(User.STATUS_NORMAL);
        user.setLoginAttempts(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        
        // 保存用户
        userDao.insert(user);
        
        // 分配角色
        if (userCreateDTO.getRoleIds() != null && !userCreateDTO.getRoleIds().isEmpty()) {
            for (Long roleId : userCreateDTO.getRoleIds()) {
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(roleId);
                userRole.setDepartmentId(userCreateDTO.getDepartmentId());
                userRole.setStatus(1);
                userRole.setStartTime(new Date());
                userRole.setCreatedBy(1L); // 系统管理员ID
                userRole.setCreateTime(new Date());
                userRole.setUpdateTime(new Date());
                userRoleDao.insert(userRole);
            }
        }
        
        return user;
    }
    
    @Override
    @Transactional
    public List<User> batchCreateUsers(List<UserCreateDTO> userCreateDTOs) {
        List<User> createdUsers = new ArrayList<>();
        for (UserCreateDTO dto : userCreateDTOs) {
            try {
                User user = createUser(dto);
                createdUsers.add(user);
            } catch (Exception e) {
                // 记录错误但继续处理其他用户
                recordOperationLog(0L, "BATCH_CREATE_USER_ERROR", "USER", "批量创建用户失败: " + dto.getUsername() + " - " + e.getMessage());
            }
        }
        return createdUsers;
    }
    
    @Override
    @Transactional
    public boolean updateUser(User user) {
        user.setUpdateTime(new Date());
        return userDao.update(user) > 0;
    }
    
    @Override
    @Transactional
    public boolean disableUser(Long userId, String reason) {
        User user = userDao.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        user.setStatus(User.STATUS_DISABLED);
        user.setUpdateTime(new Date());
        
        // 记录操作日志
        recordOperationLog(userId, "DISABLE_USER", "USER", "停用用户账号: " + reason);
        
        return userDao.update(user) > 0;
    }
    
    @Override
    @Transactional
    public boolean enableUser(Long userId) {
        User user = userDao.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        user.setStatus(User.STATUS_NORMAL);
        user.setUpdateTime(new Date());
        
        // 记录操作日志
        recordOperationLog(userId, "ENABLE_USER", "USER", "启用用户账号");
        
        return userDao.update(user) > 0;
    }
    
    @Override
    @Transactional
    public boolean deleteUser(Long userId) {
        // 检查用户是否存在
        User user = userDao.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 删除用户角色关联
        userRoleDao.deleteByUserId(userId);
        
        // 删除用户
        int result = userDao.deleteById(userId);
        
        // 记录操作日志
        recordOperationLog(userId, "DELETE_USER", "USER", "删除用户账号");
        
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean resetUserPassword(Long userId, String newPassword) {
        User user = userDao.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateTime(new Date());
        
        // 记录操作日志
        recordOperationLog(userId, "RESET_PASSWORD", "USER", "重置用户密码");
        
        return userDao.update(user) > 0;
    }
    
    @Override
    @Transactional
    public boolean lockUser(Long userId, String reason) {
        User user = userDao.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        user.setStatus(User.STATUS_LOCKED);
        user.setLockTime(new Date());
        user.setUpdateTime(new Date());
        
        // 记录操作日志
        recordOperationLog(userId, "LOCK_USER", "USER", "锁定用户账号: " + reason);
        
        return userDao.update(user) > 0;
    }
    
    @Override
    @Transactional
    public boolean unlockUser(Long userId) {
        User user = userDao.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        user.setStatus(User.STATUS_NORMAL);
        user.setLockTime(null);
        user.setUpdateTime(new Date());
        
        // 记录操作日志
        recordOperationLog(userId, "UNLOCK_USER", "USER", "解锁用户账号");
        
        return userDao.update(user) > 0;
    }
    
    // ==================== 角色权限全局管理 ====================
    
    @Override
    @Transactional
    public Role createRole(Role role) {
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
        roleDao.insert(role);
        return role;
    }
    
    @Override
    @Transactional
    public boolean updateRole(Role role) {
        role.setUpdateTime(new Date());
        return roleDao.update(role) > 0;
    }
    
    @Override
    @Transactional
    public boolean deleteRole(Long roleId) {
        // 检查是否有用户使用该角色
        List<UserRole> userRoles = userRoleDao.findByRoleId(roleId);
        if (!userRoles.isEmpty()) {
            throw new BusinessException("该角色正在被用户使用，无法删除");
        }
        
        // 删除角色权限关联
        rolePermissionDao.deleteByRoleId(roleId);
        
        // 删除角色
        return roleDao.deleteById(roleId) > 0;
    }
    
    @Override
    @Transactional
    public boolean assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        // 先删除原有权限
        rolePermissionDao.deleteByRoleId(roleId);
        
        // 分配新权限
        for (Long permissionId : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermission.setCreateTime(new Date());
            rolePermissionDao.insert(rolePermission);
        }
        
        return true;
    }
    
    @Override
    @Transactional
    public boolean createPermissionTemplate(PermissionTemplateDTO template) {
        // 权限模板的实现
        // 这里可以添加模板存储逻辑
        return true;
    }
    
    @Override
    @Transactional
    public boolean updatePermissionTemplate(PermissionTemplateDTO template) {
        // 更新权限模板的实现
        return true;
    }
    
    @Override
    @Transactional
    public boolean deletePermissionTemplate(Long templateId) {
        // 删除权限模板的实现
        return true;
    }
    
    @Override
    @Transactional
    public boolean applyPermissionTemplate(Long templateId, Long roleId) {
        // 应用权限模板的实现
        return true;
    }
    
    @Override
    @Transactional
    public boolean assignRolesToUser(Long userId, List<Long> roleIds) {
        // 先删除用户原有角色
        userRoleDao.deleteByUserId(userId);
        
        // 分配新角色
        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRole.setStatus(1);
            userRole.setStartTime(new Date());
            userRole.setCreateTime(new Date());
            userRole.setUpdateTime(new Date());
            userRoleDao.insert(userRole);
        }
        
        return true;
    }
    
    @Override
    @Transactional
    public boolean removeUserRoles(Long userId, List<Long> roleIds) {
        for (Long roleId : roleIds) {
            userRoleDao.deleteByUserIdAndRoleId(userId, roleId);
        }
        return true;
    }
    
    @Override
    @Transactional
    public boolean setTemporaryPermission(Long userId, Long roleId, String startTime, String endTime, String reason) {
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRole.setStatus(1);
        userRole.setReason(reason);
        userRole.setCreateTime(new Date());
        userRole.setUpdateTime(new Date());
        
        return userRoleDao.insert(userRole) > 0;
    }
    
    @Override
    @Transactional
    public boolean revokeTemporaryPermission(Long userRoleId) {
        return userRoleDao.deleteById(userRoleId) > 0;
    }
    
    // ==================== 院系管理员管理 ====================
    
    @Override
    @Transactional
    public User createDepartmentAdmin(UserCreateDTO userCreateDTO) {
        // 创建院系管理员的特殊逻辑
        User user = createUser(userCreateDTO);
        
        // 自动分配院系管理员角色
        Role deptAdminRole = roleDao.findByCode(Role.CODE_DEPARTMENT_ADMIN);
        if (deptAdminRole != null) {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(deptAdminRole.getId());
            userRole.setDepartmentId(userCreateDTO.getDepartmentId());
            userRole.setStatus(1);
            userRole.setStartTime(new Date());
            userRole.setCreateTime(new Date());
            userRole.setUpdateTime(new Date());
            userRoleDao.insert(userRole);
        }
        
        return user;
    }
    
    @Override
    @Transactional
    public boolean assignDepartmentAdminPermissions(Long userId, Long departmentId, List<Long> permissionIds) {
        // 为院系管理员分配特定权限
        return assignPermissionsToRole(userId, permissionIds);
    }
    
    @Override
    @Transactional
    public boolean adjustDepartmentAdminPermissions(Long userId, List<Long> permissionIds) {
        // 调整院系管理员权限
        return assignPermissionsToRole(userId, permissionIds);
    }
    
    @Override
    @Transactional
    public boolean disableDepartmentAdmin(Long userId) {
        return disableUser(userId, "院系管理员停用");
    }
    
    // ==================== 院系管理 ====================
    
    @Override
    @Transactional
    public Department createDepartment(Department department) {
        department.setCreateTime(new Date());
        department.setUpdateTime(new Date());
        departmentDao.insert(department);
        return department;
    }
    
    @Override
    @Transactional
    public boolean updateDepartment(Department department) {
        department.setUpdateTime(new Date());
        return departmentDao.update(department) > 0;
    }
    
    @Override
    @Transactional
    public boolean deleteDepartment(Long departmentId) {
        // 检查是否有用户属于该院系
        List<User> users = userDao.findByDepartmentId(departmentId);
        if (!users.isEmpty()) {
            throw new BusinessException("该院系下还有用户，无法删除");
        }
        
        return departmentDao.deleteById(departmentId) > 0;
    }
    
    @Override
    public List<Department> getDepartmentList() {
        return departmentDao.findAll();
    }
    
    // ==================== 系统监控与审计 ====================
    
    @Override
    public PageResult<OperationLog> getOperationLogs(String username, String operation, String module, 
                                                    String startTime, String endTime, int page, int size) {
        // 分页查询操作日志
        int offset = (page - 1) * size;
        List<OperationLog> logs = operationLogDao.findByConditions(username, operation, module, startTime, endTime, offset, size);
        int total = operationLogDao.countByConditions(username, operation, module, startTime, endTime);
        
        return new PageResult<>(total, logs);
    }
    
    @Override
    public String exportOperationLogs(String username, String operation, String module, 
                                     String startTime, String endTime) {
        // 导出操作日志的实现
        return "logs_export_" + System.currentTimeMillis() + ".xlsx";
    }
    
    @Override
    public List<Object> getUserLoginStatistics(String startTime, String endTime) {
        // 获取用户登录统计的实现
        List<Object> statistics = new ArrayList<>();
        // 这里可以添加具体的统计逻辑，比如：
        // - 每日登录用户数
        // - 登录失败次数
        // - 活跃用户数等
        Map<String, Object> dailyStats = new HashMap<>();
        dailyStats.put("date", "2024-01-01");
        dailyStats.put("loginCount", 150);
        dailyStats.put("activeUsers", 120);
        statistics.add(dailyStats);
        return statistics;
    }
    
    @Override
    public List<Object> getPermissionUsageStatistics(String startTime, String endTime) {
        // 获取权限使用统计的实现
        List<Object> statistics = new ArrayList<>();
        // 这里可以添加具体的统计逻辑，比如：
        // - 各权限的使用次数
        // - 权限使用趋势
        // - 热门权限排行等
        Map<String, Object> permissionStats = new HashMap<>();
        permissionStats.put("permissionName", "用户管理");
        permissionStats.put("usageCount", 500);
        permissionStats.put("usageRate", "85%");
        statistics.add(permissionStats);
        return statistics;
    }
    
    @Override
    public String generateSecurityAuditReport(String startTime, String endTime) {
        // 生成安全审计报告的实现
        return "audit_report_" + System.currentTimeMillis() + ".pdf";
    }
    
    // ==================== 查询功能 ====================
    
    @Override
    public PageResult<User> getUserList(String username, String realName, String department, 
                                       String userType, Integer status, int page, int size) {
        int offset = (page - 1) * size;
        List<User> users = userDao.findByConditions(username, realName, department, userType, status, offset, size);
        int total = userDao.countByConditions(username, realName, department, userType, status);
        return new PageResult<>(total, users);
    }
    
    @Override
    public User getUserById(Long id) {
        return userDao.findById(id);
    }
    
    @Override
    public List<Role> getRoleList() {
        return roleDao.findAll();
    }
    
    @Override
    public List<Permission> getPermissionList() {
        return permissionDao.findAll();
    }
    
    @Override
    public List<Role> getUserRoles(Long userId) {
        return roleDao.findByUserId(userId);
    }
    
    @Override
    public List<Permission> getRolePermissions(Long roleId) {
        return permissionDao.findByRoleId(roleId);
    }
    
    // ==================== 私有方法 ====================
    
    /**
     * 记录操作日志
     */
    private void recordOperationLog(Long userId, String operation, String module, String description) {
        OperationLog log = new OperationLog();
        log.setUserId(userId);
        log.setOperation(operation);
        log.setModule(module);
        log.setDescription(description);
        log.setStatus(1);
        log.setCreateTime(new Date());
        operationLogDao.insert(log);
    }
    
    // ==================== 统计功能 ====================
    
    @Override
    public int getActiveUserCount() {
        // 获取最近7天登录的用户数量
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date sevenDaysAgo = calendar.getTime();
        
        return userDao.countActiveUsers(sevenDaysAgo);
    }
    
    @Override
    public int getTodayLoginCount() {
        // 获取今日登录数量
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date todayStart = calendar.getTime();
        
        return userDao.countTodayLogins(todayStart);
    }
    
    @Override
    public AdminStatisticsDTO getStatistics() {
        AdminStatisticsDTO dto = new AdminStatisticsDTO();
        // 用户总数
        int totalUsers = userDao.countByConditions(null, null, null, null, null);
        // 活跃用户（最近7天登录）
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -7);
        Date since = cal.getTime();
        int activeUsers = userDao.countActiveUsers(since);
        // 院系数量
        int totalDepartments = departmentDao.count();
        // 今日登录
        cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date todayStart = cal.getTime();
        int todayLogins = userDao.countTodayLogins(todayStart);
        dto.setTotalUsers(totalUsers);
        dto.setActiveUsers(activeUsers);
        dto.setTotalDepartments(totalDepartments);
        dto.setTodayLogins(todayLogins);
        return dto;
    }
} 