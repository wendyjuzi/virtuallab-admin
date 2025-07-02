package com.edu.virtuallab.auth.controller;

import com.edu.virtuallab.auth.dto.UserCreateDTO;
import com.edu.virtuallab.auth.dto.PermissionTemplateDTO;
import com.edu.virtuallab.auth.model.*;
import com.edu.virtuallab.auth.service.SystemAdminService;
import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.common.api.PageResult;
import com.edu.virtuallab.common.api.StatisticsDTO;
import com.edu.virtuallab.log.model.OperationLog;
import com.edu.virtuallab.experiment.model.*;
import com.edu.virtuallab.experiment.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统管理员权限管理控制器
 */
@RestController
@RequestMapping("/system-admin")
@Api(tags = "系统管理员权限管理")
public class SystemAdminController {

    @Autowired
    private SystemAdminService systemAdminService;

    @Autowired
    private ExperimentProjectService experimentProjectService;

    @Autowired
    private ExperimentRecordService experimentRecordService;

    @Autowired
    private ExperimentReportService experimentReportService;

    @Autowired
    private ExperimentSceneService experimentSceneService;

    // ==================== 统计数据 ====================

    @GetMapping("/statistics/overview")
    @ApiOperation("获取系统概览统计")
    public CommonResult<Map<String, Object>> getSystemStatistics() {
        try {
            Map<String, Object> statistics = new HashMap<>();

            // 获取用户统计
            PageResult<User> userPage = systemAdminService.getUserList(null, null, null, null, null, 1, 1);
            statistics.put("totalUsers", userPage.getTotal());

            // 获取活跃用户数（最近7天登录的用户）
            statistics.put("activeUsers", systemAdminService.getActiveUserCount());

            // 获取院系数量
            List<Department> departments = systemAdminService.getDepartmentList();
            statistics.put("totalDepartments", departments.size());

            // 获取今日登录数
            statistics.put("todayLogins", systemAdminService.getTodayLoginCount());

            return CommonResult.success(statistics, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("获取统计数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics")
    public CommonResult<StatisticsDTO> getStatistics() {
        StatisticsDTO dto = systemAdminService.getStatistics();
        return CommonResult.success(dto, "获取统计数据成功");
    }

    // ==================== 账号全生命周期管理 ====================

    @GetMapping("/users")
    @ApiOperation("分页查询用户列表")
    public CommonResult<PageResult<User>> getUserList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String userType,
            @RequestParam(required = false) Integer status) {
        try {
            PageResult<User> result = systemAdminService.getUserList(username, realName, department, userType, status, page, size);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("获取用户列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/users/{id}")
    @ApiOperation("根据ID获取用户信息")
    public CommonResult<User> getUserById(@PathVariable Long id) {
        try {
            User user = systemAdminService.getUserById(id);
            if (user != null) {
                return CommonResult.success(user, "资源更新成功");
            } else {
                return CommonResult.failed("用户不存在");
            }
        } catch (Exception e) {
            return CommonResult.failed("获取用户信息失败: " + e.getMessage());
        }
    }

    @PostMapping("/users")
    @ApiOperation("创建用户")
    public CommonResult<User> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        try {
            User user = systemAdminService.createUser(userCreateDTO);
            return CommonResult.success(user, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("创建用户失败: " + e.getMessage());
        }
    }

    @PostMapping("/users/batch")
    @ApiOperation("批量创建用户")
    public CommonResult<List<User>> batchCreateUsers(@RequestBody List<UserCreateDTO> userCreateDTOs) {
        try {
            List<User> users = systemAdminService.batchCreateUsers(userCreateDTOs);
            return CommonResult.success(users, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("批量创建用户失败: " + e.getMessage());
        }
    }

    @PutMapping("/users")
    @ApiOperation("更新用户信息")
    public CommonResult<Boolean> updateUser(@RequestBody User user) {
        try {
            boolean result = systemAdminService.updateUser(user);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("更新用户失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/users/{id}")
    @ApiOperation("删除用户")
    public CommonResult<Boolean> deleteUser(@PathVariable Long id) {
        try {
            boolean result = systemAdminService.deleteUser(id);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("删除用户失败: " + e.getMessage());
        }
    }

    @PostMapping("/users/{id}/enable")
    @ApiOperation("启用用户")
    public CommonResult<Boolean> enableUser(@PathVariable Long id) {
        try {
            boolean result = systemAdminService.enableUser(id);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("启用用户失败: " + e.getMessage());
        }
    }

    @PostMapping("/users/{id}/disable")
    @ApiOperation("禁用用户")
    public CommonResult<Boolean> disableUser(@PathVariable Long id, @RequestParam String reason) {
        try {
            boolean result = systemAdminService.disableUser(id, reason);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("禁用用户失败: " + e.getMessage());
        }
    }

    // ==================== 角色权限全局管理 ====================

    @PostMapping("/roles")
    @ApiOperation("创建角色")
    public CommonResult<Role> createRole(@RequestBody Role role) {
        try {
            Role createdRole = systemAdminService.createRole(role);
            return CommonResult.success(createdRole, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("创建角色失败: " + e.getMessage());
        }
    }

    @PutMapping("/roles")
    @ApiOperation("更新角色")
    public CommonResult<Boolean> updateRole(@RequestBody Role role) {
        try {
            boolean result = systemAdminService.updateRole(role);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("更新角色失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/roles/{roleId}")
    @ApiOperation("删除角色")
    public CommonResult<Boolean> deleteRole(@PathVariable Long roleId) {
        try {
            boolean result = systemAdminService.deleteRole(roleId);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("删除角色失败: " + e.getMessage());
        }
    }

    @PostMapping("/roles/{roleId}/permissions")
    @ApiOperation("为角色分配权限")
    public CommonResult<Boolean> assignPermissionsToRole(@PathVariable Long roleId, @RequestBody List<Long> permissionIds) {
        try {
            boolean result = systemAdminService.assignPermissionsToRole(roleId, permissionIds);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("分配权限失败: " + e.getMessage());
        }
    }

    @PostMapping("/permission-templates")
    @ApiOperation("创建权限模板")
    public CommonResult<Boolean> createPermissionTemplate(@RequestBody PermissionTemplateDTO template) {
        try {
            boolean result = systemAdminService.createPermissionTemplate(template);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("创建权限模板失败: " + e.getMessage());
        }
    }

    @PostMapping("/users/{userId}/roles")
    @ApiOperation("为用户分配角色")
    public CommonResult<Boolean> assignRolesToUser(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        try {
            boolean result = systemAdminService.assignRolesToUser(userId, roleIds);
            return CommonResult.success(result, "资源更新成功");
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
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("设置临时权限失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/temporary-permissions/{userRoleId}")
    @ApiOperation("回收临时权限")
    public CommonResult<Boolean> revokeTemporaryPermission(@PathVariable Long userRoleId) {
        try {
            boolean result = systemAdminService.revokeTemporaryPermission(userRoleId);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("回收临时权限失败: " + e.getMessage());
        }
    }

    // ==================== 院系管理员管理 ====================

    @PostMapping("/department-admins")
    @ApiOperation("创建院系管理员")
    public CommonResult<User> createDepartmentAdmin(@RequestBody UserCreateDTO userCreateDTO) {
        try {
            User user = systemAdminService.createDepartmentAdmin(userCreateDTO);
            return CommonResult.success(user, "资源更新成功");
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
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("分配权限失败: " + e.getMessage());
        }
    }

    @PostMapping("/department-admins/{userId}/adjust-permissions")
    @ApiOperation("调整院系管理员权限")
    public CommonResult<Boolean> adjustDepartmentAdminPermissions(@PathVariable Long userId,
                                                                 @RequestBody List<Long> permissionIds) {
        try {
            boolean result = systemAdminService.adjustDepartmentAdminPermissions(userId, permissionIds);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("调整权限失败: " + e.getMessage());
        }
    }

    @PostMapping("/department-admins/{userId}/disable")
    @ApiOperation("停用院系管理员")
    public CommonResult<Boolean> disableDepartmentAdmin(@PathVariable Long userId) {
        try {
            boolean result = systemAdminService.disableDepartmentAdmin(userId);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("停用院系管理员失败: " + e.getMessage());
        }
    }

    // ==================== 院系管理 ====================

    @PostMapping("/departments")
    @ApiOperation("创建院系")
    public CommonResult<Department> createDepartment(@RequestBody Department department) {
        try {
            Department createdDept = systemAdminService.createDepartment(department);
            return CommonResult.success(createdDept, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("创建院系失败: " + e.getMessage());
        }
    }

    @PutMapping("/departments")
    @ApiOperation("更新院系信息")
    public CommonResult<Boolean> updateDepartment(@RequestBody Department department) {
        try {
            boolean result = systemAdminService.updateDepartment(department);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("更新院系失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/departments/{departmentId}")
    @ApiOperation("删除院系")
    public CommonResult<Boolean> deleteDepartment(@PathVariable Long departmentId) {
        try {
            boolean result = systemAdminService.deleteDepartment(departmentId);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("删除院系失败: " + e.getMessage());
        }
    }

    @GetMapping("/departments")
    @ApiOperation("获取院系列表")
    public CommonResult<List<Department>> getDepartmentList() {
        try {
            List<Department> departments = systemAdminService.getDepartmentList();
            return CommonResult.success(departments, "资源更新成功");
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
            return CommonResult.success(logs, "资源更新成功");
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
            String exportFile = systemAdminService.exportOperationLogs(username, operation, module, startTime, endTime);
            return CommonResult.success(exportFile, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("导出操作日志失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics/user-login")
    @ApiOperation("获取用户登录统计")
    public CommonResult<List<Object>> getUserLoginStatistics(@RequestParam String startTime, @RequestParam String endTime) {
        try {
            List<Object> statistics = systemAdminService.getUserLoginStatistics(startTime, endTime);
            return CommonResult.success(statistics, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("获取登录统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics/permission-usage")
    @ApiOperation("获取权限使用统计")
    public CommonResult<List<Object>> getPermissionUsageStatistics(@RequestParam String startTime, @RequestParam String endTime) {
        try {
            List<Object> statistics = systemAdminService.getPermissionUsageStatistics(startTime, endTime);
            return CommonResult.success(statistics, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("获取权限使用统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/audit-report")
    @ApiOperation("生成安全审计报告")
    public CommonResult<String> generateSecurityAuditReport(@RequestParam String startTime, @RequestParam String endTime) {
        try {
            String report = systemAdminService.generateSecurityAuditReport(startTime, endTime);
            return CommonResult.success(report, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("生成审计报告失败: " + e.getMessage());
        }
    }

    // ==================== 查询功能 ====================

    @GetMapping("/roles")
    @ApiOperation("获取角色列表")
    public CommonResult<List<Role>> getRoleList() {
        try {
            List<Role> roles = systemAdminService.getRoleList();
            return CommonResult.success(roles, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("获取角色列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/permissions")
    @ApiOperation("获取权限列表")
    public CommonResult<List<Permission>> getPermissionList() {
        try {
            List<Permission> permissions = systemAdminService.getPermissionList();
            return CommonResult.success(permissions, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("获取权限列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/users/{userId}/roles")
    @ApiOperation("获取用户角色")
    public CommonResult<List<Role>> getUserRoles(@PathVariable Long userId) {
        try {
            List<Role> roles = systemAdminService.getUserRoles(userId);
            return CommonResult.success(roles, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("获取用户角色失败: " + e.getMessage());
        }
    }

    @GetMapping("/roles/{roleId}/permissions")
    @ApiOperation("获取角色权限")
    public CommonResult<List<Permission>> getRolePermissions(@PathVariable Long roleId) {
        try {
            List<Permission> permissions = systemAdminService.getRolePermissions(roleId);
            return CommonResult.success(permissions, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("获取角色权限失败: " + e.getMessage());
        }
    }

    // ==================== 实验项目管理 ====================

    @GetMapping("/experiments")
    @ApiOperation("分页查询实验项目列表")
    public CommonResult<PageResult<ExperimentProject>> getExperimentList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String keyword) {
        try {
            List<ExperimentProject> projects = experimentProjectService.search(category, level, keyword);
            int total = projects.size();
            int start = (page - 1) * size;
            int end = Math.min(start + size, total);
            List<ExperimentProject> pageData = projects.subList(start, end);
            PageResult<ExperimentProject> result = new PageResult<>(total, pageData);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("获取实验项目列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/experiments/{id}")
    @ApiOperation("根据ID获取实验项目")
    public CommonResult<ExperimentProject> getExperimentById(@PathVariable Long id) {
        try {
            ExperimentProject project = experimentProjectService.getById(id);
            if (project != null) {
                return CommonResult.success(project, "资源更新成功");
            } else {
                return CommonResult.failed("实验项目不存在");
            }
        } catch (Exception e) {
            return CommonResult.failed("获取实验项目失败: " + e.getMessage());
        }
    }

    @PostMapping("/experiments")
    @ApiOperation("创建实验项目")
    public CommonResult<Integer> createExperiment(@RequestBody ExperimentProject project) {
        try {
            int result = experimentProjectService.create(project);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("创建实验项目失败: " + e.getMessage());
        }
    }

    @PutMapping("/experiments")
    @ApiOperation("更新实验项目")
    public CommonResult<Integer> updateExperiment(@RequestBody ExperimentProject project) {
        try {
            int result = experimentProjectService.update(project);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("更新实验项目失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/experiments/{id}")
    @ApiOperation("删除实验项目")
    public CommonResult<Integer> deleteExperiment(@PathVariable Long id) {
        try {
            int result = experimentProjectService.delete(id);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("删除实验项目失败: " + e.getMessage());
        }
    }

    // ==================== 实验记录管理 ====================

    @GetMapping("/experiment-records")
    @ApiOperation("获取实验记录列表")
    public CommonResult<List<ExperimentRecord>> getExperimentRecords() {
        try {
            List<ExperimentRecord> records = experimentRecordService.listAll();
            return CommonResult.success(records, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("获取实验记录失败: " + e.getMessage());
        }
    }

    @GetMapping("/experiment-records/{id}")
    @ApiOperation("根据ID获取实验记录")
    public CommonResult<ExperimentRecord> getExperimentRecordById(@PathVariable Long id) {
        try {
            ExperimentRecord record = experimentRecordService.getById(id);
            if (record != null) {
                return CommonResult.success(record, "资源更新成功");
            } else {
                return CommonResult.failed("实验记录不存在");
            }
        } catch (Exception e) {
            return CommonResult.failed("获取实验记录失败: " + e.getMessage());
        }
    }

    @PostMapping("/experiment-records")
    @ApiOperation("创建实验记录")
    public CommonResult<Integer> createExperimentRecord(@RequestBody ExperimentRecord record) {
        try {
            int result = experimentRecordService.create(record);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("创建实验记录失败: " + e.getMessage());
        }
    }

    @PutMapping("/experiment-records")
    @ApiOperation("更新实验记录")
    public CommonResult<Integer> updateExperimentRecord(@RequestBody ExperimentRecord record) {
        try {
            int result = experimentRecordService.update(record);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("更新实验记录失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/experiment-records/{id}")
    @ApiOperation("删除实验记录")
    public CommonResult<Integer> deleteExperimentRecord(@PathVariable Long id) {
        try {
            int result = experimentRecordService.delete(id);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("删除实验记录失败: " + e.getMessage());
        }
    }

    // ==================== 实验报告管理 ====================

    @GetMapping("/experiment-reports")
    @ApiOperation("获取实验报告列表")
    public CommonResult<List<ExperimentReport>> getExperimentReports() {
        try {
            List<ExperimentReport> reports = experimentReportService.listAll();
            return CommonResult.success(reports, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("获取实验报告失败: " + e.getMessage());
        }
    }

    @GetMapping("/experiment-reports/{id}")
    @ApiOperation("根据ID获取实验报告")
    public CommonResult<ExperimentReport> getExperimentReportById(@PathVariable Long id) {
        try {
            ExperimentReport report = experimentReportService.getById(id);
            if (report != null) {
                return CommonResult.success(report, "资源更新成功");
            } else {
                return CommonResult.failed("实验报告不存在");
            }
        } catch (Exception e) {
            return CommonResult.failed("获取实验报告失败: " + e.getMessage());
        }
    }

    @PostMapping("/experiment-reports")
    @ApiOperation("创建实验报告")
    public CommonResult<Integer> createExperimentReport(@RequestBody ExperimentReport report) {
        try {
            int result = experimentReportService.create(report);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("创建实验报告失败: " + e.getMessage());
        }
    }

    @PutMapping("/experiment-reports")
    @ApiOperation("更新实验报告")
    public CommonResult<Integer> updateExperimentReport(@RequestBody ExperimentReport report) {
        try {
            int result = experimentReportService.update(report);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("更新实验报告失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/experiment-reports/{id}")
    @ApiOperation("删除实验报告")
    public CommonResult<Integer> deleteExperimentReport(@PathVariable Long id) {
        try {
            int result = experimentReportService.delete(id);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("删除实验报告失败: " + e.getMessage());
        }
    }

    // ==================== 实验场景管理 ====================

    @GetMapping("/experiment-scenes")
    @ApiOperation("获取实验场景列表")
    public CommonResult<List<ExperimentScene>> getExperimentScenes() {
        try {
            List<ExperimentScene> scenes = experimentSceneService.listAll();
            return CommonResult.success(scenes, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("获取实验场景失败: " + e.getMessage());
        }
    }

    @GetMapping("/experiment-scenes/{id}")
    @ApiOperation("根据ID获取实验场景")
    public CommonResult<ExperimentScene> getExperimentSceneById(@PathVariable Long id) {
        try {
            ExperimentScene scene = experimentSceneService.getById(id);
            if (scene != null) {
                return CommonResult.success(scene, "资源更新成功");
            } else {
                return CommonResult.failed("实验场景不存在");
            }
        } catch (Exception e) {
            return CommonResult.failed("获取实验场景失败: " + e.getMessage());
        }
    }

    @PostMapping("/experiment-scenes")
    @ApiOperation("创建实验场景")
    public CommonResult<Integer> createExperimentScene(@RequestBody ExperimentScene scene) {
        try {
            int result = experimentSceneService.create(scene);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("创建实验场景失败: " + e.getMessage());
        }
    }

    @PutMapping("/experiment-scenes")
    @ApiOperation("更新实验场景")
    public CommonResult<Integer> updateExperimentScene(@RequestBody ExperimentScene scene) {
        try {
            int result = experimentSceneService.update(scene);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("更新实验场景失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/experiment-scenes/{id}")
    @ApiOperation("删除实验场景")
    public CommonResult<Integer> deleteExperimentScene(@PathVariable Long id) {
        try {
            int result = experimentSceneService.delete(id);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("删除实验场景失败: " + e.getMessage());
        }
    }
}