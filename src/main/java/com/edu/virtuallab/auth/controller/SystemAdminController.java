package com.edu.virtuallab.auth.controller;

import com.edu.virtuallab.auth.dto.UserCreateDTO;
import com.edu.virtuallab.auth.dto.PermissionTemplateDTO;
import com.edu.virtuallab.auth.model.*;
import com.edu.virtuallab.auth.service.SystemAdminService;
import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.common.api.PageResult;
import com.edu.virtuallab.log.model.OperationLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统管理员权限管理控制器
 */
@RestController
@RequestMapping("/system-admin")
@Api(tags = "系统管理员权限管理")
public class SystemAdminController {
    
    @Autowired
    private SystemAdminService systemAdminService;
    
    // ==================== 账号全生命周期管理 ====================
    
    @PostMapping("/users")
    @ApiOperation("创建用户账号")
    public CommonResult<User> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        try {
            User user = systemAdminService.createUser(userCreateDTO);
            return CommonResult.success(user);
        } catch (Exception e) {
            return CommonResult.failed("创建用户失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/users/batch")
    @ApiOperation("批量创建用户账号")
    public CommonResult<List<User>> batchCreateUsers(@RequestBody List<UserCreateDTO> userCreateDTOs) {
        try {
            List<User> users = systemAdminService.batchCreateUsers(userCreateDTOs);
            return CommonResult.success(users);
        } catch (Exception e) {
            return CommonResult.failed("批量创建用户失败: " + e.getMessage());
        }
    }
    
    @PutMapping("/users")
    @ApiOperation("修改用户信息")
    public CommonResult<Boolean> updateUser(@RequestBody User user) {
        try {
            boolean result = systemAdminService.updateUser(user);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("修改用户失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/users/{userId}/disable")
    @ApiOperation("停用用户账号")
    public CommonResult<Boolean> disableUser(@PathVariable Long userId, @RequestParam String reason) {
        try {
            boolean result = systemAdminService.disableUser(userId, reason);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("停用用户失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/users/{userId}/enable")
    @ApiOperation("启用用户账号")
    public CommonResult<Boolean> enableUser(@PathVariable Long userId) {
        try {
            boolean result = systemAdminService.enableUser(userId);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("启用用户失败: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/users/{userId}")
    @ApiOperation("删除用户账号")
    public CommonResult<Boolean> deleteUser(@PathVariable Long userId) {
        try {
            boolean result = systemAdminService.deleteUser(userId);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("删除用户失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/users/{userId}/reset-password")
    @ApiOperation("重置用户密码")
    public CommonResult<Boolean> resetUserPassword(@PathVariable Long userId, @RequestParam String newPassword) {
        try {
            boolean result = systemAdminService.resetUserPassword(userId, newPassword);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("重置密码失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/users/{userId}/lock")
    @ApiOperation("锁定用户账号")
    public CommonResult<Boolean> lockUser(@PathVariable Long userId, @RequestParam String reason) {
        try {
            boolean result = systemAdminService.lockUser(userId, reason);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("锁定用户失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/users/{userId}/unlock")
    @ApiOperation("解锁用户账号")
    public CommonResult<Boolean> unlockUser(@PathVariable Long userId) {
        try {
            boolean result = systemAdminService.unlockUser(userId);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("解锁用户失败: " + e.getMessage());
        }
    }
    
    // ==================== 角色权限全局管理 ====================
    
    @PostMapping("/roles")
    @ApiOperation("创建角色")
    public CommonResult<Role> createRole(@RequestBody Role role) {
        try {
            Role createdRole = systemAdminService.createRole(role);
            return CommonResult.success(createdRole);
        } catch (Exception e) {
            return CommonResult.failed("创建角色失败: " + e.getMessage());
        }
    }
    
    @PutMapping("/roles")
    @ApiOperation("更新角色")
    public CommonResult<Boolean> updateRole(@RequestBody Role role) {
        try {
            boolean result = systemAdminService.updateRole(role);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("更新角色失败: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/roles/{roleId}")
    @ApiOperation("删除角色")
    public CommonResult<Boolean> deleteRole(@PathVariable Long roleId) {
        try {
            boolean result = systemAdminService.deleteRole(roleId);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("删除角色失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/roles/{roleId}/permissions")
    @ApiOperation("为角色分配权限")
    public CommonResult<Boolean> assignPermissionsToRole(@PathVariable Long roleId, @RequestBody List<Long> permissionIds) {
        try {
            boolean result = systemAdminService.assignPermissionsToRole(roleId, permissionIds);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("分配权限失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/permission-templates")
    @ApiOperation("创建权限模板")
    public CommonResult<Boolean> createPermissionTemplate(@RequestBody PermissionTemplateDTO template) {
        try {
            boolean result = systemAdminService.createPermissionTemplate(template);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("创建权限模板失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/users/{userId}/roles")
    @ApiOperation("为用户分配角色")
    public CommonResult<Boolean> assignRolesToUser(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        try {
            boolean result = systemAdminService.assignRolesToUser(userId, roleIds);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("分配角色失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/users/{userId}/temporary-permissions")
    @ApiOperation("设置临时权限")
    public CommonResult<Boolean> setTemporaryPermission(@PathVariable Long userId, 
                                                       @RequestParam Long roleId,
                                                       @RequestParam String startTime,
                                                       @RequestParam String endTime,
                                                       @RequestParam String reason) {
        try {
            boolean result = systemAdminService.setTemporaryPermission(userId, roleId, startTime, endTime, reason);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("设置临时权限失败: " + e.getMessage());
        }
    }
    
    // ==================== 院系管理员管理 ====================
    
    @PostMapping("/department-admins")
    @ApiOperation("创建院系管理员")
    public CommonResult<User> createDepartmentAdmin(@RequestBody UserCreateDTO userCreateDTO) {
        try {
            User user = systemAdminService.createDepartmentAdmin(userCreateDTO);
            return CommonResult.success(user);
        } catch (Exception e) {
            return CommonResult.failed("创建院系管理员失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/department-admins/{userId}/permissions")
    @ApiOperation("为院系管理员分配权限")
    public CommonResult<Boolean> assignDepartmentAdminPermissions(@PathVariable Long userId,
                                                                 @RequestParam Long departmentId,
                                                                 @RequestBody List<Long> permissionIds) {
        try {
            boolean result = systemAdminService.assignDepartmentAdminPermissions(userId, departmentId, permissionIds);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("分配权限失败: " + e.getMessage());
        }
    }
    
    // ==================== 院系管理 ====================
    
    @PostMapping("/departments")
    @ApiOperation("创建院系")
    public CommonResult<Department> createDepartment(@RequestBody Department department) {
        try {
            Department createdDepartment = systemAdminService.createDepartment(department);
            return CommonResult.success(createdDepartment);
        } catch (Exception e) {
            return CommonResult.failed("创建院系失败: " + e.getMessage());
        }
    }
    
    @PutMapping("/departments")
    @ApiOperation("更新院系信息")
    public CommonResult<Boolean> updateDepartment(@RequestBody Department department) {
        try {
            boolean result = systemAdminService.updateDepartment(department);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("更新院系失败: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/departments/{departmentId}")
    @ApiOperation("删除院系")
    public CommonResult<Boolean> deleteDepartment(@PathVariable Long departmentId) {
        try {
            boolean result = systemAdminService.deleteDepartment(departmentId);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("删除院系失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/departments")
    @ApiOperation("获取院系列表")
    public CommonResult<List<Department>> getDepartmentList() {
        try {
            List<Department> departments = systemAdminService.getDepartmentList();
            return CommonResult.success(departments);
        } catch (Exception e) {
            return CommonResult.failed("获取院系列表失败: " + e.getMessage());
        }
    }
    
    // ==================== 系统监控与审计 ====================
    
    @GetMapping("/operation-logs")
    @ApiOperation("获取操作日志")
    public CommonResult<PageResult<OperationLog>> getOperationLogs(@RequestParam(required = false) String username,
                                                                  @RequestParam(required = false) String operation,
                                                                  @RequestParam(required = false) String module,
                                                                  @RequestParam(required = false) String startTime,
                                                                  @RequestParam(required = false) String endTime,
                                                                  @RequestParam(defaultValue = "1") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        try {
            PageResult<OperationLog> logs = systemAdminService.getOperationLogs(username, operation, module, startTime, endTime, page, size);
            return CommonResult.success(logs);
        } catch (Exception e) {
            return CommonResult.failed("获取操作日志失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/operation-logs/export")
    @ApiOperation("导出操作日志")
    public CommonResult<String> exportOperationLogs(@RequestParam(required = false) String username,
                                                   @RequestParam(required = false) String operation,
                                                   @RequestParam(required = false) String module,
                                                   @RequestParam(required = false) String startTime,
                                                   @RequestParam(required = false) String endTime) {
        try {
            String filePath = systemAdminService.exportOperationLogs(username, operation, module, startTime, endTime);
            return CommonResult.success(filePath);
        } catch (Exception e) {
            return CommonResult.failed("导出操作日志失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/statistics/user-login")
    @ApiOperation("获取用户登录统计")
    public CommonResult<List<Object>> getUserLoginStatistics(@RequestParam String startTime, @RequestParam String endTime) {
        try {
            List<Object> statistics = systemAdminService.getUserLoginStatistics(startTime, endTime);
            return CommonResult.success(statistics);
        } catch (Exception e) {
            return CommonResult.failed("获取登录统计失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/statistics/permission-usage")
    @ApiOperation("获取权限使用统计")
    public CommonResult<List<Object>> getPermissionUsageStatistics(@RequestParam String startTime, @RequestParam String endTime) {
        try {
            List<Object> statistics = systemAdminService.getPermissionUsageStatistics(startTime, endTime);
            return CommonResult.success(statistics);
        } catch (Exception e) {
            return CommonResult.failed("获取权限使用统计失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/audit-report")
    @ApiOperation("生成安全审计报告")
    public CommonResult<String> generateSecurityAuditReport(@RequestParam String startTime, @RequestParam String endTime) {
        try {
            String reportPath = systemAdminService.generateSecurityAuditReport(startTime, endTime);
            return CommonResult.success(reportPath);
        } catch (Exception e) {
            return CommonResult.failed("生成审计报告失败: " + e.getMessage());
        }
    }
    
    // ==================== 查询功能 ====================
    
    @GetMapping("/users")
    @ApiOperation("分页查询用户列表")
    public CommonResult<PageResult<User>> getUserList(@RequestParam(required = false) String username,
                                                     @RequestParam(required = false) String realName,
                                                     @RequestParam(required = false) String department,
                                                     @RequestParam(required = false) String userType,
                                                     @RequestParam(required = false) Integer status,
                                                     @RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        try {
            PageResult<User> users = systemAdminService.getUserList(username, realName, department, userType, status, page, size);
            return CommonResult.success(users);
        } catch (Exception e) {
            return CommonResult.failed("查询用户列表失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/roles")
    @ApiOperation("获取角色列表")
    public CommonResult<List<Role>> getRoleList() {
        try {
            List<Role> roles = systemAdminService.getRoleList();
            return CommonResult.success(roles);
        } catch (Exception e) {
            return CommonResult.failed("获取角色列表失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/permissions")
    @ApiOperation("获取权限列表")
    public CommonResult<List<Permission>> getPermissionList() {
        try {
            List<Permission> permissions = systemAdminService.getPermissionList();
            return CommonResult.success(permissions);
        } catch (Exception e) {
            return CommonResult.failed("获取权限列表失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/users/{userId}/roles")
    @ApiOperation("获取用户角色")
    public CommonResult<List<Role>> getUserRoles(@PathVariable Long userId) {
        try {
            List<Role> roles = systemAdminService.getUserRoles(userId);
            return CommonResult.success(roles);
        } catch (Exception e) {
            return CommonResult.failed("获取用户角色失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/roles/{roleId}/permissions")
    @ApiOperation("获取角色权限")
    public CommonResult<List<Permission>> getRolePermissions(@PathVariable Long roleId) {
        try {
            List<Permission> permissions = systemAdminService.getRolePermissions(roleId);
            return CommonResult.success(permissions);
        } catch (Exception e) {
            return CommonResult.failed("获取角色权限失败: " + e.getMessage());
        }
    }
} 