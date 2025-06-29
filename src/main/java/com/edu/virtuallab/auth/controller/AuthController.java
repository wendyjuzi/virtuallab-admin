package com.edu.virtuallab.auth.controller;

import com.edu.virtuallab.auth.model.User;
import com.edu.virtuallab.auth.service.AuthService;
import com.edu.virtuallab.common.api.CommonResult;
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

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/login/password")
    public CommonResult<Map<String, Object>> loginWithPassword(@RequestBody LoginDTO loginDTO) {
        CommonResult<Map<String, Object>> loginResult = authService.loginWithPassword(loginDTO.getUsername(), loginDTO.getPassword());
        if (loginResult.getCode() != 200 || loginResult.getData() == null) {
            return CommonResult.failed(loginResult.getMessage());
        }
        Map<String, Object> result = loginResult.getData();
        return CommonResult.success(result);
    }

    @PostMapping("/login/sms")
    public CommonResult<Boolean> loginWithSms(@RequestParam String phone, @RequestParam String code) {
        return authService.loginWithSms(phone, code);
    }

    @PostMapping("/login/fingerprint")
    public CommonResult<Boolean> loginWithFingerprint(@RequestParam String username, @RequestParam String fingerprintData) {
        return authService.loginWithFingerprint(username, fingerprintData);
    }

    @PostMapping("/sendSmsCode")
    public CommonResult<Boolean> sendSmsCode(@RequestParam String phone) {
        return authService.sendSmsCode(phone);
    }

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