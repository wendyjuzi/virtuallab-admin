package com.edu.virtuallab.auth.controller;

import com.edu.virtuallab.auth.model.User;
import com.edu.virtuallab.auth.service.AuthService;
import com.edu.virtuallab.auth.service.UserService;
import com.edu.virtuallab.common.api.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import com.edu.virtuallab.auth.model.LoginDTO;
import com.edu.virtuallab.auth.model.Role;
import com.edu.virtuallab.auth.service.RoleService;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.edu.virtuallab.auth.service.AuthFactorService;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthFactorService authFactorService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login/password")
    public CommonResult<Map<String, Object>> loginWithPassword(@RequestBody LoginDTO loginDTO) {
        CommonResult<Map<String, Object>> loginResult = authService.loginWithPassword(loginDTO.getUsername(), loginDTO.getPassword());
        if (loginResult.getCode() != 200 || loginResult.getData() == null) {
            return CommonResult.failed(loginResult.getMessage());
        }
        Map<String, Object> result = loginResult.getData();
        return CommonResult.success(result);
    }

    @PostMapping("/login")
    @ApiOperation("登录（用户名或邮箱）")
    public CommonResult<User> login(@RequestParam String account, @RequestParam String password) {
        User user = userService.getByUsername(account);
        if (user == null) {
            user = userService.getByEmail(account);
        }
        if (user == null) {
            return CommonResult.failed("用户不存在");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return CommonResult.failed("密码错误");
        }

        return CommonResult.success(user);
    }

    @PostMapping("/login/email")
    @ApiOperation("邮箱验证码登录")
    public CommonResult<Map<String, Object>> loginByEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        return authService.loginWithEmail(email, code);
    }

    @PostMapping("/sendEmailCode")
    @ApiOperation("发送邮箱验证码")
    public CommonResult<Boolean> sendEmailCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String type = request.get("type");
        return authService.sendEmailCode(email, type);
    }

    @PostMapping("/login/sms")
    public CommonResult<Boolean> loginWithSms(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String code = request.get("code");
        return authService.loginWithSms(phone, code);
    }

    @PostMapping("/login/fingerprint")
    public CommonResult<Boolean> loginWithFingerprint(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String fingerprintData = request.get("fingerprintData");
        return authService.loginWithFingerprint(username, fingerprintData);
    }

    // 暂时注释掉短信注册功能
    /*
    @PostMapping("/sendSmsCode")
    public CommonResult<Boolean> sendSmsCode(@RequestParam String phone, @RequestParam(required = false) String userType) {
        if (userType == null || !"admin".equalsIgnoreCase(userType)) {
            return CommonResult.failed("只有管理员注册时才允许发送短信验证码");
        }
        boolean result = authFactorService.sendSmsCodeForRegister(phone);
        return CommonResult.success(result);
    }
    */

    @GetMapping("/currentUser")
    public CommonResult<Map<String, Object>> getCurrentUser() {
        User user = authService.getCurrentUser();
        if (user == null) {
            return CommonResult.failed("未登录");
        }
        List<Role> roles = roleService.getRolesByUserId(user.getId());
        List<String> roleCodes = roles.stream().map(Role::getCode).collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("roles", roleCodes);
        return CommonResult.success(result);
    }
} 