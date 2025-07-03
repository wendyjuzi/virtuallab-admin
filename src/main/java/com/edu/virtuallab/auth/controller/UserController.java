package com.edu.virtuallab.auth.controller;

import com.edu.virtuallab.auth.model.User;
import com.edu.virtuallab.auth.model.UserRegisterDTO;
import com.edu.virtuallab.auth.service.EmailVerificationService;
import com.edu.virtuallab.auth.service.UserService;
import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.common.api.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@Api(tags = "用户管理")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @GetMapping("/{id}")
    @ApiOperation("根据ID获取用户信息")
    public CommonResult<User> getById(@PathVariable Long id) {
        try {
            User user = userService.getById(id);
            if (user != null) {
                return CommonResult.success(user, "资源更新成功");
            } else {
                return CommonResult.failed("用户不存在");
            }
        } catch (Exception e) {
            return CommonResult.failed("获取用户信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/username/{username}")
    @ApiOperation("根据用户名获取用户信息")
    public CommonResult<User> getByUsername(@PathVariable String username) {
        try {
            User user = userService.getByUsername(username);
            if (user != null) {
                return CommonResult.success(user, "资源更新成功");
            } else {
                return CommonResult.failed("用户不存在");
            }
        } catch (Exception e) {
            return CommonResult.failed("获取用户信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    @ApiOperation("获取所有用户列表")
    public CommonResult<List<User>> listAll() {
        try {
            List<User> users = userService.listAll();
            return CommonResult.success(users, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("获取用户列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/page")
    @ApiOperation("分页查询用户列表")
    public CommonResult<PageResult<User>> getUserList(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String userType,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            PageResult<User> result = userService.getUserList(username, realName, department, userType, status, page, size);
            return CommonResult.success(result, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("查询用户列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    @ApiOperation("创建用户")
    public CommonResult<Boolean> createUser(@RequestBody User user) {
        try {
            boolean result = userService.createUser(user);
            if (result) {
                return CommonResult.success(true, "资源更新成功");
            } else {
                return CommonResult.failed("创建用户失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("创建用户失败: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    @ApiOperation("用户注册")
    public CommonResult<Boolean> register(@RequestBody UserRegisterDTO dto) {
        try {
        boolean success = userService.register(dto);
        if (success) {
                return CommonResult.success(true, "资源更新成功");
        } else {
            return CommonResult.failed("注册失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("注册失败: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    @ApiOperation("更新用户信息")
    public CommonResult<Boolean> update(@RequestBody User user) {
        try {
            boolean result = userService.update(user);
            if (result) {
                return CommonResult.success(true, "资源更新成功");
            } else {
                return CommonResult.failed("更新失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除用户")
    public CommonResult<Boolean> delete(@PathVariable Long id) {
        try {
            boolean result = userService.delete(id);
            if (result) {
                return CommonResult.success(true, "资源更新成功");
            } else {
                return CommonResult.failed("删除失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("删除失败: " + e.getMessage());
        }
    }

    @GetMapping("/profile/{userId}")
    @ApiOperation("获取当前用户个人信息")
    public CommonResult<User> getCurrentUserProfile(@PathVariable Long userId) {
        try {
            User user = userService.getCurrentUserProfile(userId);
            if (user != null) {
                return CommonResult.success(user, "资源更新成功");
            } else {
                return CommonResult.failed("用户不存在");
            }
        } catch (Exception e) {
            return CommonResult.failed("获取个人信息失败: " + e.getMessage());
        }
    }

    @PutMapping("/profile/update")
    @ApiOperation("更新个人信息")
    public CommonResult<Boolean> updateProfile(@RequestBody User user) {
        try {
            boolean result = userService.updateProfile(user);
            if (result) {
                return CommonResult.success(true, "资源更新成功");
            } else {
                return CommonResult.failed("更新个人信息失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("更新个人信息失败: " + e.getMessage());
        }
    }

    @PutMapping("/profile/avatar")
    @ApiOperation("更新头像")
    public CommonResult<Boolean> updateAvatar(@RequestParam Long userId, @RequestParam String avatarUrl) {
        try {
            boolean result = userService.updateAvatar(userId, avatarUrl);
            if (result) {
                return CommonResult.success(true, "资源更新成功");
            } else {
                return CommonResult.failed("更新头像失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("更新头像失败: " + e.getMessage());
        }
    }

    @PostMapping("/profile/change-password")
    @ApiOperation("修改密码")
    public CommonResult<Boolean> changePassword(@RequestParam Long userId, 
                                               @RequestParam String oldPassword, 
                                               @RequestParam String newPassword) {
        try {
            boolean result = userService.changePassword(userId, oldPassword, newPassword);
            if (result) {
                return CommonResult.success(true, "资源更新成功");
            } else {
                return CommonResult.failed("修改密码失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("修改密码失败: " + e.getMessage());
        }
    }

    @PostMapping("/profile/reset-password")
    @ApiOperation("重置密码")
    public CommonResult<Boolean> resetPassword(@RequestParam Long userId, @RequestParam String newPassword) {
        try {
            boolean result = userService.resetPassword(userId, newPassword);
            if (result) {
                return CommonResult.success(true, "资源更新成功");
            } else {
                return CommonResult.failed("重置密码失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("重置密码失败: " + e.getMessage());
        }
    }

    @PostMapping("/profile/update-email")
    @ApiOperation("更新邮箱")
    public CommonResult<Boolean> updateEmail(@RequestParam Long userId, 
                                            @RequestParam String newEmail, 
                                            @RequestParam String verificationCode) {
        try {
            boolean result = userService.updateEmail(userId, newEmail, verificationCode);
            if (result) {
                return CommonResult.success(true, "资源更新成功");
            } else {
                return CommonResult.failed("更新邮箱失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("更新邮箱失败: " + e.getMessage());
        }
    }

    @PostMapping("/profile/update-phone")
    @ApiOperation("更新手机号")
    public CommonResult<Boolean> updatePhone(@RequestParam Long userId, 
                                            @RequestParam String newPhone, 
                                            @RequestParam String verificationCode) {
        try {
            boolean result = userService.updatePhone(userId, newPhone, verificationCode);
            if (result) {
                return CommonResult.success(true, "资源更新成功");
            } else {
                return CommonResult.failed("更新手机号失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("更新手机号失败: " + e.getMessage());
        }
    }

    @PostMapping("/{userId}/enable")
    @ApiOperation("启用用户")
    public CommonResult<Boolean> enableUser(@PathVariable Long userId) {
        try {
            boolean result = userService.enableUser(userId);
            if (result) {
                return CommonResult.success(true, "资源更新成功");
            } else {
                return CommonResult.failed("启用用户失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("启用用户失败: " + e.getMessage());
        }
    }

    @PostMapping("/{userId}/disable")
    @ApiOperation("禁用用户")
    public CommonResult<Boolean> disableUser(@PathVariable Long userId, @RequestParam String reason) {
        try {
            boolean result = userService.disableUser(userId, reason);
            if (result) {
                return CommonResult.success(true, "资源更新成功");
            } else {
                return CommonResult.failed("禁用用户失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("禁用用户失败: " + e.getMessage());
        }
    }

    @PostMapping("/{userId}/lock")
    @ApiOperation("锁定用户")
    public CommonResult<Boolean> lockUser(@PathVariable Long userId, @RequestParam String reason) {
        try {
            boolean result = userService.lockUser(userId, reason);
            if (result) {
                return CommonResult.success(true, "资源更新成功");
            } else {
                return CommonResult.failed("锁定用户失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("锁定用户失败: " + e.getMessage());
        }
    }

    @PostMapping("/{userId}/unlock")
    @ApiOperation("解锁用户")
    public CommonResult<Boolean> unlockUser(@PathVariable Long userId) {
        try {
            boolean result = userService.unlockUser(userId);
            if (result) {
                return CommonResult.success(true, "资源更新成功");
            } else {
                return CommonResult.failed("解锁用户失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("解锁用户失败: " + e.getMessage());
        }
    }

    @PostMapping("/{userId}/status")
    @ApiOperation("更新用户状态")
    public CommonResult<Boolean> updateUserStatus(@PathVariable Long userId, @RequestParam Integer status) {
        try {
            boolean result = userService.updateUserStatus(userId, status);
            if (result) {
                return CommonResult.success(true, "资源更新成功");
            } else {
                return CommonResult.failed("更新用户状态失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("更新用户状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/{userId}/roles")
    @ApiOperation("为用户分配角色")
    public CommonResult<Boolean> assignRoles(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        try {
            boolean result = userService.assignRoles(userId, roleIds);
            if (result) {
                return CommonResult.success(true, "资源更新成功");
            } else {
                return CommonResult.failed("分配角色失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("分配角色失败: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}/roles")
    @ApiOperation("获取用户角色列表")
    public CommonResult<List<Long>> getUserRoleIds(@PathVariable Long userId) {
        try {
            List<Long> roleIds = userService.getUserRoleIds(userId);
            return CommonResult.success(roleIds, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("获取用户角色失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/roles")
    @ApiOperation("移除用户角色")
    public CommonResult<Boolean> removeUserRoles(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        try {
            boolean result = userService.removeUserRoles(userId, roleIds);
            if (result) {
                return CommonResult.success(true, "资源更新成功");
            } else {
                return CommonResult.failed("移除角色失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("移除角色失败: " + e.getMessage());
        }
    }

    @GetMapping("/check/username/{username}")
    @ApiOperation("检查用户名是否存在")
    public CommonResult<Boolean> isUsernameExists(@PathVariable String username) {
        try {
            boolean exists = userService.isUsernameExists(username);
            return CommonResult.success(exists, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("检查用户名失败: " + e.getMessage());
        }
    }

    @GetMapping("/check/email/{email}")
    @ApiOperation("检查邮箱是否存在")
    public CommonResult<Boolean> isEmailExists(@PathVariable String email) {
        try {
            boolean exists = userService.isEmailExists(email);
            return CommonResult.success(exists, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("检查邮箱失败: " + e.getMessage());
        }
    }

    @GetMapping("/check/phone/{phone}")
    @ApiOperation("检查手机号是否存在")
    public CommonResult<Boolean> isPhoneExists(@PathVariable String phone) {
        try {
            boolean exists = userService.isPhoneExists(phone);
            return CommonResult.success(exists, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("检查手机号失败: " + e.getMessage());
        }
    }

    @GetMapping("/check/student-id/{studentId}")
    @ApiOperation("检查学号/工号是否存在")
    public CommonResult<Boolean> isStudentIdExists(@PathVariable String studentId) {
        try {
            boolean exists = userService.isStudentIdExists(studentId);
            return CommonResult.success(exists, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("检查学号/工号失败: " + e.getMessage());
        }
    }

    @GetMapping("/student/{studentId}")
    @ApiOperation("根据学号获取用户信息")
    public CommonResult<User> getUserByStudentId(@PathVariable String studentId) {
        User user = userService.findByStudentId(studentId);
        if (user != null) {
            return CommonResult.success(user); // 直接返回 User 实体
        }
        return CommonResult.failed("用户不存在");
    }

    @PostMapping("/logout")
    @ApiOperation("退出登录")
    public CommonResult<Boolean> logout(HttpServletRequest request) {
        try {
            // 这里可以添加登出逻辑，比如清除token等
            return CommonResult.success(true, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("退出登录失败: " + e.getMessage());
        }
    }
}