package com.edu.virtuallab.auth.controller;

import com.edu.virtuallab.auth.model.TempPermission;
import com.edu.virtuallab.auth.service.TempPermissionService;
import com.edu.virtuallab.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 临时权限管理控制器
 * 用于管理用户的临时权限分配、撤销、查询等功能
 */
@RestController
@RequestMapping("/temp-permission")
public class TempPermissionController {
    
    @Autowired
    private TempPermissionService tempPermissionService;
    
    /**
     * 分配临时权限
     * @param tempPermission 临时权限信息
     * @return 分配结果
     */
    @PostMapping("/grant")
    public CommonResult<Boolean> grantTempPermission(@RequestBody TempPermission tempPermission) {
        try {
            boolean result = tempPermissionService.grantTempPermission(tempPermission);
            if (result) {
                return CommonResult.success(true, "资源更新成功");
            } else {
                return CommonResult.failed("临时权限分配失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("临时权限分配异常：" + e.getMessage());
        }
    }
    
    /**
     * 撤销临时权限
     * @param id 临时权限ID
     * @return 撤销结果
     */
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> revokeTempPermission(@PathVariable Long id) {
        try {
            boolean result = tempPermissionService.revokeTempPermission(id);
            if (result) {
                return CommonResult.success(true, "资源更新成功");
            } else {
                return CommonResult.failed("临时权限撤销失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("临时权限撤销异常：" + e.getMessage());
        }
    }
    
    /**
     * 批量撤销用户临时权限
     * @param userId 用户ID
     * @return 撤销结果
     */
    @DeleteMapping("/user/{userId}")
    public CommonResult<Boolean> revokeByUserId(@PathVariable Long userId) {
        try {
            boolean result = tempPermissionService.revokeByUserId(userId);
            if (result) {
                return CommonResult.success(true, "资源更新成功");
            } else {
                return CommonResult.failed("用户临时权限撤销失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("用户临时权限撤销异常：" + e.getMessage());
        }
    }
    
    /**
     * 根据用户ID查询临时权限
     * @param userId 用户ID
     * @return 临时权限列表
     */
    @GetMapping("/user/{userId}")
    public CommonResult<List<TempPermission>> findByUserId(@PathVariable Long userId) {
        try {
            List<TempPermission> tempPermissions = tempPermissionService.findByUserId(userId);
            return CommonResult.success(tempPermissions, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("查询用户临时权限异常：" + e.getMessage());
        }
    }
    
    /**
     * 查询用户生效中的临时权限
     * @param userId 用户ID
     * @return 生效中的临时权限列表
     */
    @GetMapping("/user/{userId}/active")
    public CommonResult<List<TempPermission>> findActiveByUserId(@PathVariable Long userId) {
        try {
            List<TempPermission> tempPermissions = tempPermissionService.findActiveByUserId(userId);
            return CommonResult.success(tempPermissions, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("查询用户生效临时权限异常：" + e.getMessage());
        }
    }
    
    /**
     * 根据角色ID查询临时权限
     * @param roleId 角色ID
     * @return 临时权限列表
     */
    @GetMapping("/role/{roleId}")
    public CommonResult<List<TempPermission>> findByRoleId(@PathVariable Long roleId) {
        try {
            List<TempPermission> tempPermissions = tempPermissionService.findByRoleId(roleId);
            return CommonResult.success(tempPermissions, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("查询角色临时权限异常：" + e.getMessage());
        }
    }
    
    /**
     * 根据权限ID查询临时权限
     * @param permissionId 权限ID
     * @return 临时权限列表
     */
    @GetMapping("/permission/{permissionId}")
    public CommonResult<List<TempPermission>> findByPermissionId(@PathVariable Long permissionId) {
        try {
            List<TempPermission> tempPermissions = tempPermissionService.findByPermissionId(permissionId);
            return CommonResult.success(tempPermissions, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("查询权限临时分配异常：" + e.getMessage());
        }
    }
    
    /**
     * 检查用户是否有临时权限
     * @param userId 用户ID
     * @param permissionId 权限ID
     * @return 是否有临时权限
     */
    @GetMapping("/check-permission")
    public CommonResult<Boolean> hasTempPermission(@RequestParam Long userId, @RequestParam Long permissionId) {
        try {
            boolean hasPermission = tempPermissionService.hasTempPermission(userId, permissionId);
            return CommonResult.success(hasPermission, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("检查临时权限异常：" + e.getMessage());
        }
    }
    
    /**
     * 检查用户是否有临时角色
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 是否有临时角色
     */
    @GetMapping("/check-role")
    public CommonResult<Boolean> hasTempRole(@RequestParam Long userId, @RequestParam Long roleId) {
        try {
            boolean hasRole = tempPermissionService.hasTempRole(userId, roleId);
            return CommonResult.success(hasRole, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("检查临时角色异常：" + e.getMessage());
        }
    }
    
    /**
     * 获取用户所有生效的临时权限ID列表
     * @param userId 用户ID
     * @return 权限ID列表
     */
    @GetMapping("/user/{userId}/permission-ids")
    public CommonResult<List<Long>> getActivePermissionIds(@PathVariable Long userId) {
        try {
            List<Long> permissionIds = tempPermissionService.getActivePermissionIds(userId);
            return CommonResult.success(permissionIds, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("获取用户临时权限ID异常：" + e.getMessage());
        }
    }
    
    /**
     * 获取用户所有生效的临时角色ID列表
     * @param userId 用户ID
     * @return 角色ID列表
     */
    @GetMapping("/user/{userId}/role-ids")
    public CommonResult<List<Long>> getActiveRoleIds(@PathVariable Long userId) {
        try {
            List<Long> roleIds = tempPermissionService.getActiveRoleIds(userId);
            return CommonResult.success(roleIds, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("获取用户临时角色ID异常：" + e.getMessage());
        }
    }
    
    /**
     * 更新过期权限状态
     * @return 更新结果
     */
    @PostMapping("/update-expired")
    public CommonResult<String> updateExpiredPermissions() {
        try {
            tempPermissionService.updateExpiredPermissions();
            return CommonResult.success("过期权限状态更新成功", "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("更新过期权限状态异常：" + e.getMessage());
        }
    }
    
    /**
     * 批量分配临时权限
     * @param tempPermissions 临时权限列表
     * @return 分配结果
     */
    @PostMapping("/batch-grant")
    public CommonResult<Boolean> batchGrantTempPermission(@RequestBody List<TempPermission> tempPermissions) {
        try {
            boolean allSuccess = true;
            for (TempPermission tempPermission : tempPermissions) {
                if (!tempPermissionService.grantTempPermission(tempPermission)) {
                    allSuccess = false;
                    break;
                }
            }
            if (allSuccess) {
                return CommonResult.success(true, "资源更新成功");
            } else {
                return CommonResult.failed("批量分配临时权限失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("批量分配临时权限异常：" + e.getMessage());
        }
    }
} 