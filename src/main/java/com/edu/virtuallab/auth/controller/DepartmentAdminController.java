package com.edu.virtuallab.auth.controller;

import com.edu.virtuallab.auth.dto.UserCreateDTO;
import com.edu.virtuallab.auth.model.*;
import com.edu.virtuallab.auth.service.DepartmentAdminService;
import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.common.api.PageResult;
import com.edu.virtuallab.log.model.OperationLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 院系管理员权限管理控制器
 */
@RestController
@RequestMapping("/department-admin")
@Api(tags = "院系管理员权限管理")
public class DepartmentAdminController {
    
    @Autowired
    private DepartmentAdminService departmentAdminService;
    
    // ==================== 本院系账号管理 ====================
    
    @PostMapping("/users")
    @ApiOperation("创建本院系用户账号")
    public CommonResult<User> createDepartmentUser(@RequestBody UserCreateDTO userCreateDTO,
                                                  @RequestParam Long adminUserId) {
        try {
            User user = departmentAdminService.createDepartmentUser(userCreateDTO, adminUserId);
            return CommonResult.success(user);
        } catch (Exception e) {
            return CommonResult.failed("创建用户失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/users/batch")
    @ApiOperation("批量创建本院系用户账号")
    public CommonResult<List<User>> batchCreateDepartmentUsers(@RequestBody List<UserCreateDTO> userCreateDTOs,
                                                              @RequestParam Long adminUserId) {
        try {
            List<User> users = departmentAdminService.batchCreateDepartmentUsers(userCreateDTOs, adminUserId);
            return CommonResult.success(users);
        } catch (Exception e) {
            return CommonResult.failed("批量创建用户失败: " + e.getMessage());
        }
    }
    
    @PutMapping("/users")
    @ApiOperation("修改本院系用户信息")
    public CommonResult<Boolean> updateDepartmentUser(@RequestBody User user, @RequestParam Long adminUserId) {
        try {
            boolean result = departmentAdminService.updateDepartmentUser(user, adminUserId);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("修改用户失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/users/{userId}/disable")
    @ApiOperation("停用本院系用户账号")
    public CommonResult<Boolean> disableDepartmentUser(@PathVariable Long userId,
                                                      @RequestParam String reason,
                                                      @RequestParam Long adminUserId) {
        try {
            boolean result = departmentAdminService.disableDepartmentUser(userId, reason, adminUserId);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("停用用户失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/users/{userId}/enable")
    @ApiOperation("启用本院系用户账号")
    public CommonResult<Boolean> enableDepartmentUser(@PathVariable Long userId, @RequestParam Long adminUserId) {
        try {
            boolean result = departmentAdminService.enableDepartmentUser(userId, adminUserId);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("启用用户失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/users/{userId}/reset-password")
    @ApiOperation("重置本院系用户密码")
    public CommonResult<Boolean> resetDepartmentUserPassword(@PathVariable Long userId,
                                                            @RequestParam String newPassword,
                                                            @RequestParam Long adminUserId) {
        try {
            boolean result = departmentAdminService.resetDepartmentUserPassword(userId, newPassword, adminUserId);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("重置密码失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/users/{userId}/lock")
    @ApiOperation("锁定本院系用户账号")
    public CommonResult<Boolean> lockDepartmentUser(@PathVariable Long userId,
                                                   @RequestParam String reason,
                                                   @RequestParam Long adminUserId) {
        try {
            boolean result = departmentAdminService.lockDepartmentUser(userId, reason, adminUserId);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("锁定用户失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/users/{userId}/unlock")
    @ApiOperation("解锁本院系用户账号")
    public CommonResult<Boolean> unlockDepartmentUser(@PathVariable Long userId, @RequestParam Long adminUserId) {
        try {
            boolean result = departmentAdminService.unlockDepartmentUser(userId, adminUserId);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("解锁用户失败: " + e.getMessage());
        }
    }
    
    // ==================== 本院系权限管理 ====================
    
    @PostMapping("/users/{userId}/roles")
    @ApiOperation("为本院系用户分配角色")
    public CommonResult<Boolean> assignRolesToDepartmentUser(@PathVariable Long userId,
                                                            @RequestBody List<Long> roleIds,
                                                            @RequestParam Long adminUserId) {
        try {
            boolean result = departmentAdminService.assignRolesToDepartmentUser(userId, roleIds, adminUserId);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("分配角色失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/users/{userId}/temporary-permissions")
    @ApiOperation("为本院系用户设置临时权限")
    public CommonResult<Boolean> setDepartmentUserTemporaryPermission(@PathVariable Long userId,
                                                                     @RequestParam Long roleId,
                                                                     @RequestParam String startTime,
                                                                     @RequestParam String endTime,
                                                                     @RequestParam String reason,
                                                                     @RequestParam Long adminUserId) {
        try {
            boolean result = departmentAdminService.setDepartmentUserTemporaryPermission(userId, roleId, startTime, endTime, reason, adminUserId);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("设置临时权限失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/users/{userId}/permissions/adjust")
    @ApiOperation("调整本院系用户权限")
    public CommonResult<Boolean> adjustDepartmentUserPermissions(@PathVariable Long userId,
                                                                @RequestBody List<Long> permissionIds,
                                                                @RequestParam Long adminUserId) {
        try {
            boolean result = departmentAdminService.adjustDepartmentUserPermissions(userId, permissionIds, adminUserId);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("调整权限失败: " + e.getMessage());
        }
    }
    
    // ==================== 教学资源与数据管理 ====================
    
    @PostMapping("/resources/{resourceId}")
    @ApiOperation("管理本院系教学资源")
    public CommonResult<Boolean> manageDepartmentResource(@PathVariable Long resourceId,
                                                         @RequestParam String operation,
                                                         @RequestParam Long adminUserId) {
        try {
            boolean result = departmentAdminService.manageDepartmentResource(resourceId, operation, adminUserId);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("管理资源失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/resources/{resourceId}/approve")
    @ApiOperation("审核本院系教师提交的资源")
    public CommonResult<Boolean> approveDepartmentResource(@PathVariable Long resourceId,
                                                          @RequestParam String approvalResult,
                                                          @RequestParam String comment,
                                                          @RequestParam Long adminUserId) {
        try {
            boolean result = departmentAdminService.approveDepartmentResource(resourceId, approvalResult, comment, adminUserId);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("审核资源失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/scores/{scoreId}")
    @ApiOperation("管理本院系学生成绩数据")
    public CommonResult<Boolean> manageDepartmentScore(@PathVariable Long scoreId,
                                                      @RequestParam String operation,
                                                      @RequestParam Long adminUserId) {
        try {
            boolean result = departmentAdminService.manageDepartmentScore(scoreId, operation, adminUserId);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("管理成绩失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/scores/report")
    @ApiOperation("生成本院系成绩报表")
    public CommonResult<String> generateDepartmentScoreReport(@RequestParam Long adminUserId) {
        try {
            String reportPath = departmentAdminService.generateDepartmentScoreReport(adminUserId);
            return CommonResult.success(reportPath);
        } catch (Exception e) {
            return CommonResult.failed("生成成绩报表失败: " + e.getMessage());
        }
    }
    
    // ==================== 与系统管理员协同 ====================
    
    @PostMapping("/permission-requests")
    @ApiOperation("提交权限申请")
    public CommonResult<Boolean> submitPermissionRequest(@RequestParam String requestType,
                                                        @RequestParam String description,
                                                        @RequestBody List<Long> userIds,
                                                        @RequestBody List<Long> permissionIds,
                                                        @RequestParam Long adminUserId) {
        try {
            boolean result = departmentAdminService.submitPermissionRequest(requestType, description, userIds, permissionIds, adminUserId);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("提交权限申请失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/user-abnormal")
    @ApiOperation("上报账号异常")
    public CommonResult<Boolean> reportUserAbnormal(@RequestParam Long userId,
                                                   @RequestParam String abnormalType,
                                                   @RequestParam String description,
                                                   @RequestParam Long adminUserId) {
        try {
            boolean result = departmentAdminService.reportUserAbnormal(userId, abnormalType, description, adminUserId);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("上报异常失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/permission-requests/status")
    @ApiOperation("获取权限申请状态")
    public CommonResult<List<Object>> getPermissionRequestStatus(@RequestParam Long adminUserId) {
        try {
            List<Object> status = departmentAdminService.getPermissionRequestStatus(adminUserId);
            return CommonResult.success(status);
        } catch (Exception e) {
            return CommonResult.failed("获取申请状态失败: " + e.getMessage());
        }
    }
    
    // ==================== 查询功能 ====================
    
    @GetMapping("/users")
    @ApiOperation("分页查询本院系用户列表")
    public CommonResult<PageResult<User>> getDepartmentUserList(@RequestParam(required = false) String username,
                                                               @RequestParam(required = false) String realName,
                                                               @RequestParam(required = false) String userType,
                                                               @RequestParam(required = false) Integer status,
                                                               @RequestParam Long adminUserId,
                                                               @RequestParam(defaultValue = "1") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        try {
            PageResult<User> users = departmentAdminService.getDepartmentUserList(username, realName, userType, status, adminUserId, page, size);
            return CommonResult.success(users);
        } catch (Exception e) {
            return CommonResult.failed("查询用户列表失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/users/{userId}/roles")
    @ApiOperation("获取本院系用户角色")
    public CommonResult<List<Role>> getDepartmentUserRoles(@PathVariable Long userId, @RequestParam Long adminUserId) {
        try {
            List<Role> roles = departmentAdminService.getDepartmentUserRoles(userId, adminUserId);
            return CommonResult.success(roles);
        } catch (Exception e) {
            return CommonResult.failed("获取用户角色失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/assignable-roles")
    @ApiOperation("获取本院系可分配的角色列表")
    public CommonResult<List<Role>> getDepartmentAssignableRoles(@RequestParam Long adminUserId) {
        try {
            List<Role> roles = departmentAdminService.getDepartmentAssignableRoles(adminUserId);
            return CommonResult.success(roles);
        } catch (Exception e) {
            return CommonResult.failed("获取可分配角色失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/assignable-permissions")
    @ApiOperation("获取本院系可分配的权限列表")
    public CommonResult<List<Permission>> getDepartmentAssignablePermissions(@RequestParam Long adminUserId) {
        try {
            List<Permission> permissions = departmentAdminService.getDepartmentAssignablePermissions(adminUserId);
            return CommonResult.success(permissions);
        } catch (Exception e) {
            return CommonResult.failed("获取可分配权限失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/operation-logs")
    @ApiOperation("获取本院系操作日志")
    public CommonResult<PageResult<OperationLog>> getDepartmentOperationLogs(@RequestParam(required = false) String username,
                                                                             @RequestParam(required = false) String operation,
                                                                             @RequestParam(required = false) String module,
                                                                             @RequestParam(required = false) String startTime,
                                                                             @RequestParam(required = false) String endTime,
                                                                             @RequestParam Long adminUserId,
                                                                             @RequestParam(defaultValue = "1") int page,
                                                                             @RequestParam(defaultValue = "10") int size) {
        try {
            PageResult<OperationLog> logs = departmentAdminService.getDepartmentOperationLogs(username, operation, module, startTime, endTime, adminUserId, page, size);
            return CommonResult.success(logs);
        } catch (Exception e) {
            return CommonResult.failed("获取操作日志失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/statistics/users")
    @ApiOperation("获取本院系用户统计")
    public CommonResult<Object> getDepartmentUserStatistics(@RequestParam Long adminUserId) {
        try {
            Object statistics = departmentAdminService.getDepartmentUserStatistics(adminUserId);
            return CommonResult.success(statistics);
        } catch (Exception e) {
            return CommonResult.failed("获取用户统计失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/statistics/permission-usage")
    @ApiOperation("获取本院系权限使用统计")
    public CommonResult<Object> getDepartmentPermissionUsageStatistics(@RequestParam Long adminUserId) {
        try {
            Object statistics = departmentAdminService.getDepartmentPermissionUsageStatistics(adminUserId);
            return CommonResult.success(statistics);
        } catch (Exception e) {
            return CommonResult.failed("获取权限使用统计失败: " + e.getMessage());
        }
    }
} 