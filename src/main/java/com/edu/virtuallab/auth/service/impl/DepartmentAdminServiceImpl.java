package com.edu.virtuallab.auth.service.impl;

import com.edu.virtuallab.auth.dao.*;
import com.edu.virtuallab.auth.dto.UserCreateDTO;
import com.edu.virtuallab.auth.model.*;
import com.edu.virtuallab.auth.service.*;
import com.edu.virtuallab.common.api.PageResult;
import com.edu.virtuallab.common.exception.BusinessException;
import com.edu.virtuallab.log.dao.OperationLogDao;
import com.edu.virtuallab.log.model.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DepartmentAdminServiceImpl implements DepartmentAdminService {

    @Autowired private UserDao userDao;
    @Autowired private RoleDao roleDao;
    @Autowired private UserRoleDao userRoleDao;
    @Autowired private DepartmentDao departmentDao;
    @Autowired private PermissionDao permissionDao;
    @Autowired private TempPermissionService tempPermissionService;
    @Autowired private OperationLogDao operationLogDao;

    @Override
    @Transactional
    public User createDepartmentUser(UserCreateDTO dto, Long adminUserId) {
        Department adminDept = getAdminDepartment(adminUserId);
        if (!adminDept.getId().equals(dto.getDepartmentId())) {
            throw new BusinessException("无权为其他院系创建用户");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setRealName(dto.getRealName());
        user.setStudentId(dto.getStudentId());
        user.setDepartment(adminDept.getName());
        user.setMajor(dto.getMajor());
        user.setGrade(dto.getGrade());
        user.setClassName(dto.getClassName());
        user.setStatus(User.STATUS_NORMAL);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userDao.insert(user);

        if (dto.getRoleIds() != null) {
            for (Long roleId : dto.getRoleIds()) {
                UserRole ur = new UserRole();
                ur.setUserId(user.getId());
                ur.setRoleId(roleId);
                ur.setDepartmentId(adminDept.getId());
                ur.setStatus(1);
                ur.setCreatedBy(adminUserId);
                ur.setCreateTime(new Date());
                ur.setUpdateTime(new Date());
                userRoleDao.insert(ur);
            }
        }

        logOperation(adminUserId, "创建用户", user.getUsername(), "院系用户管理");
        return user;
    }

    @Override
    @Transactional
    public List<User> batchCreateDepartmentUsers(List<UserCreateDTO> dtos, Long adminUserId) {
        return dtos.stream()
                .map(dto -> createDepartmentUser(dto, adminUserId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean updateDepartmentUser(User user, Long adminUserId) {
        Department adminDept = getAdminDepartment(adminUserId);
        checkSameDepartment(user, adminDept, "无权修改其他院系用户");
        user.setUpdateTime(new Date());
        return userDao.update(user) > 0;
    }

    @Override
    @Transactional
    public boolean disableDepartmentUser(Long userId, String reason, Long adminUserId) {
        User user = userDao.findById(userId);
        Department adminDept = getAdminDepartment(adminUserId);
        checkSameDepartment(user, adminDept, "无权禁用其他院系用户");
        user.setStatus(User.STATUS_DISABLED);
        user.setUpdateTime(new Date());
        return userDao.update(user) > 0;
    }

    @Override
    @Transactional
    public boolean enableDepartmentUser(Long userId, Long adminUserId) {
        User user = userDao.findById(userId);
        Department adminDept = getAdminDepartment(adminUserId);
        checkSameDepartment(user, adminDept, "无权启用其他院系用户");
        user.setStatus(User.STATUS_NORMAL);
        user.setUpdateTime(new Date());
        return userDao.update(user) > 0;
    }

    @Override
    @Transactional
    public boolean resetDepartmentUserPassword(Long userId, String newPassword, Long adminUserId) {
        User user = userDao.findById(userId);
        Department adminDept = getAdminDepartment(adminUserId);
        checkSameDepartment(user, adminDept, "无权重置其他院系用户密码");
        user.setPassword(newPassword);
        user.setUpdateTime(new Date());
        return userDao.update(user) > 0;
    }

    @Override
    @Transactional
    public boolean lockDepartmentUser(Long userId, String reason, Long adminUserId) {
        User user = userDao.findById(userId);
        Department adminDept = getAdminDepartment(adminUserId);
        checkSameDepartment(user, adminDept, "无权锁定其他院系用户");
        user.setStatus(User.STATUS_LOCKED);
        user.setLockTime(new Date());
        user.setUpdateTime(new Date());
        return userDao.update(user) > 0;
    }

    @Override
    @Transactional
    public boolean unlockDepartmentUser(Long userId, Long adminUserId) {
        User user = userDao.findById(userId);
        Department adminDept = getAdminDepartment(adminUserId);
        checkSameDepartment(user, adminDept, "无权解锁其他院系用户");
        user.setStatus(User.STATUS_NORMAL);
        user.setLockTime(null);
        user.setUpdateTime(new Date());
        return userDao.update(user) > 0;
    }

    @Override
    @Transactional
    public boolean assignRolesToDepartmentUser(Long userId, List<Long> roleIds, Long adminUserId) {
        Department adminDept = getAdminDepartment(adminUserId);
        userRoleDao.deleteByUserIdAndDepartmentId(userId, adminDept.getId());
        for (Long roleId : roleIds) {
            UserRole ur = new UserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            ur.setDepartmentId(adminDept.getId());
            ur.setStatus(1);
            ur.setCreatedBy(adminUserId);
            ur.setCreateTime(new Date());
            ur.setUpdateTime(new Date());
            userRoleDao.insert(ur);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean removeDepartmentUserRoles(Long userId, List<Long> roleIds, Long adminUserId) {
        Department adminDept = getAdminDepartment(adminUserId);
        for (Long roleId : roleIds) {
            userRoleDao.deleteByUserIdAndRoleIdAndDepartmentId(userId, roleId, adminDept.getId());
        }
        return true;
    }

    @Override
    @Transactional
    public boolean setDepartmentUserTemporaryPermission(Long userId, Long roleId, String start, String end, String reason, Long adminUserId) {
        TempPermission temp = new TempPermission();
        temp.setUserId(userId);
        temp.setRoleId(roleId);
        temp.setStartTime(parseDate(start));
        temp.setEndTime(parseDate(end));
        temp.setReason(reason);
        temp.setGrantBy(adminUserId);
        temp.setStatus(TempPermission.STATUS_ACTIVE);
        temp.setCreateTime(new Date());
        temp.setUpdateTime(new Date());
        return tempPermissionService.grantTempPermission(temp);
    }

    @Override
    @Transactional
    public boolean revokeDepartmentUserTemporaryPermission(Long userRoleId, Long adminUserId) {
        return tempPermissionService.revokeTempPermission(userRoleId);
    }

    @Override
    public boolean adjustDepartmentUserPermissions(Long userId, List<Long> permissionIds, Long adminUserId) {
        Department adminDept = getAdminDepartment(adminUserId);
        User user = userDao.findById(userId);
        checkSameDepartment(user, adminDept, "无权修改其他院系用户权限");
        permissionDao.clearUserPermissions(userId);
        permissionDao.assignPermissionsToUser(userId, permissionIds);
        return true;
    }

    @Override
    public boolean manageDepartmentResource(Long resourceId, String operation, Long adminUserId) {
        // 示例：根据operation（add/edit/delete）进行资源管理
        logOperation(adminUserId, "资源操作:" + operation, "资源ID: " + resourceId, "资源管理");
        return true;
    }

    @Override
    public boolean approveDepartmentResource(Long resourceId, String approvalResult, String comment, Long adminUserId) {
        logOperation(adminUserId, "资源审核", "资源ID: " + resourceId + ", 结果: " + approvalResult, "资源审核");
        return true;
    }

    @Override
    public boolean manageDepartmentScore(Long scoreId, String operation, Long adminUserId) {
        logOperation(adminUserId, "成绩操作: " + operation, "成绩ID: " + scoreId, "成绩管理");
        return true;
    }

    @Override
    public String generateDepartmentScoreReport(Long adminUserId) {
        logOperation(adminUserId, "生成成绩报表", "", "成绩报表");
        return "成绩报表生成成功";
    }

    @Override
    public boolean submitPermissionRequest(String requestType, String description, List<Long> userIds, List<Long> permissionIds, Long adminUserId) {
        logOperation(adminUserId, "提交权限申请", "类型: " + requestType + ", 用户: " + userIds, "权限管理");
        return true;
    }

    @Override
    public boolean reportUserAbnormal(Long userId, String abnormalType, String description, Long adminUserId) {
        logOperation(adminUserId, "异常上报", "用户ID: " + userId + ", 类型: " + abnormalType, "异常管理");
        return true;
    }

    @Override
    public List<Object> getPermissionRequestStatus(Long adminUserId) {
        logOperation(adminUserId, "查询权限申请状态", "", "权限申请");
        return new ArrayList<>();
    }

    @Override
    public PageResult<User> getDepartmentUserList(String username, String realName, String userType, Integer status, Long adminUserId, int page, int size) {
        Department adminDept = getAdminDepartment(adminUserId);
        int offset = (page - 1) * size;
        List<User> users = userDao.findByConditions(username, realName, adminDept.getName(), userType, status, offset, size);
        int total = userDao.countByConditions(username, realName, adminDept.getName(), userType, status);
        return new PageResult<>(total, users);
    }

    @Override
    public List<Role> getDepartmentUserRoles(Long userId, Long adminUserId) {
        Department adminDept = getAdminDepartment(adminUserId);
        List<UserRole> userRoles = userRoleDao.findByUserIdAndDepartmentId(userId, adminDept.getId());
        return userRoles.stream()
                .map(ur -> roleDao.findById(ur.getRoleId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Role> getDepartmentAssignableRoles(Long adminUserId) {
        // 院系管理员可以分配的角色：教师、学生、访客等
        return roleDao.findAll().stream()
                .filter(role -> !"SYSTEM_ADMIN".equals(role.getCode()) && !"DEPARTMENT_ADMIN".equals(role.getCode()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> getDepartmentAssignablePermissions(Long adminUserId) {
        // 院系管理员可以分配的权限：教学相关权限
        return permissionDao.findAll().stream()
                .filter(permission -> permission.getCode().startsWith("TEACH_") || 
                                    permission.getCode().startsWith("STUDENT_") ||
                                    permission.getCode().startsWith("EXPERIMENT_"))
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<OperationLog> getDepartmentOperationLogs(String username, String operation, String module, String startTime, String endTime, Long adminUserId, int page, int size) {
        Department adminDept = getAdminDepartment(adminUserId);
        int offset = (page - 1) * size;
        List<OperationLog> list = operationLogDao.findByConditions(username, operation, module, startTime, endTime, offset, size);
        long total = operationLogDao.countByConditions(username, operation, module, startTime, endTime);
        return new PageResult<>(total, list);
    }

    @Override
    public Object getDepartmentUserStatistics(Long adminUserId) {
        return Map.of("total", userDao.countByDepartment(getAdminDepartment(adminUserId).getName()));
    }

    @Override
    public Object getDepartmentPermissionUsageStatistics(Long adminUserId) {
        return permissionDao.countPermissionUsageByDepartment(getAdminDepartment(adminUserId).getId());
    }

    private Department getAdminDepartment(Long adminUserId) {
        User admin = userDao.findById(adminUserId);
        if (admin == null) throw new BusinessException("管理员不存在");
        return departmentDao.findAll().stream()
                .filter(d -> d.getName().equals(admin.getDepartment()))
                .findFirst()
                .orElseThrow(() -> new BusinessException("未找到管理员所属院系"));
    }

    private void checkSameDepartment(User user, Department adminDept, String errMsg) {
        if (user == null || adminDept == null || !adminDept.getName().equals(user.getDepartment())) {
            throw new BusinessException(errMsg);
        }
    }

    private Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    private void logOperation(Long userId, String operation, String target, String module) {
        OperationLog log = new OperationLog();
        log.setUserId(userId);
        log.setOperation(operation);
        log.setTarget(target);
        log.setModule(module);
        log.setCreateTime(new Date());
        operationLogDao.insert(log);
    }
}
