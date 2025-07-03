package com.edu.virtuallab.auth.service.impl;

import com.edu.virtuallab.auth.model.User;
import com.edu.virtuallab.auth.service.AuthService;
import com.edu.virtuallab.auth.dao.UserDao;
import com.edu.virtuallab.auth.service.EmailVerificationService;
import com.edu.virtuallab.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.edu.virtuallab.auth.util.JwtUtil;
import com.edu.virtuallab.auth.model.Role;
import com.edu.virtuallab.auth.service.RoleService;

/**
 * AuthService接口的实现类，提供用户认证相关功能
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleService roleService;
    @Autowired
    private EmailVerificationService emailVerificationService;

    /**
     * 密码登录方法
     *
     * @param username 用户名
     * @param password 密码
     * @return 包含用户信息、token和角色的CommonResult
     */
    @Override
    public CommonResult<Map<String, Object>> loginWithPassword(String username, String password) {
        // 1. 查询用户
        User user = userDao.findByUsername(username);
        if (user == null) {
            return CommonResult.failed("用户不存在");
        }

        // 2. 密码校验（支持bcrypt加密和明文两种方式）
        if (user.getPassword() != null && user.getPassword().startsWith("$2a$")) {
            // BCrypt加密方式校验
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (!encoder.matches(password, user.getPassword())) {
                return CommonResult.failed("密码错误");
            }
        } else {
            // 明文方式校验（不推荐在生产环境使用）
            if (!password.equals(user.getPassword())) {
                return CommonResult.failed("密码错误");
            }
        }

        // 3. 校验用户状态
        if (user.getStatus() == null || user.getStatus() != User.STATUS_NORMAL) {
            return CommonResult.failed("用户状态异常");
        }

        // 4. 设置Spring Security认证信息
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 5. 生成JWT token
        String token = JwtUtil.generateToken(user.getUsername());

        // 6. 查询用户角色
        List<Role> roles = roleService.getRolesByUserId(user.getId());
        List<String> roleCodes = roles.stream().map(Role::getCode).toList();

        // 7. 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        result.put("roles", roleCodes);
        return CommonResult.success(result, "资源更新成功");
    }
    
    /**
     * 指纹登录方法
     * @param username 用户名
     * @param fingerprintData 指纹数据
     * @return 登录结果
     */
    @Override
    public CommonResult<Boolean> loginWithFingerprint(String username, String fingerprintData) {
        // 注意：生产环境需要对接指纹识别设备或服务
        // 这里仅做演示，固定指纹数据为"valid"
        if ("valid".equals(fingerprintData)) {
            return CommonResult.success(Boolean.TRUE, "资源更新成功");
        }
        return CommonResult.failed("指纹识别失败");
    }

    /**
     * 邮箱验证码登录方法
     * @param email 邮箱
     * @param code 验证码
     * @return 登录结果
     */
    @Override
    public CommonResult<Map<String, Object>> loginWithEmail(String email, String code) {
        // 1. 验证邮箱验证码
        CommonResult<Boolean> verifyResult = emailVerificationService.verifyCode(email, code, "LOGIN");
        if (!verifyResult.isSuccess()) {
            return CommonResult.failed(verifyResult.getMessage());
        }

        // 2. 查询用户
        User user = userDao.findByEmail(email);
        if (user == null) {
            return CommonResult.failed("用户不存在");
        }

        // 3. 校验用户状态
        if (user.getStatus() == null || user.getStatus() != User.STATUS_NORMAL) {
            return CommonResult.failed("用户状态异常");
        }

        // 4. 设置Spring Security认证信息
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 5. 生成JWT token
        String token = JwtUtil.generateToken(user.getUsername());

        // 6. 查询用户角色
        List<Role> roles = roleService.getRolesByUserId(user.getId());
        List<String> roleCodes = roles.stream().map(Role::getCode).toList();

        // 7. 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        result.put("roles", roleCodes);
        return CommonResult.success(result, "资源更新成功");
    }

    /**
     * 发送邮箱验证码
     * @param email 邮箱
     * @param type 验证码类型
     * @return 发送结果
     */
    @Override
    public CommonResult<Boolean> sendEmailCode(String email, String type) {
        return emailVerificationService.sendVerificationCode(email, type);
    }

    /**
     * 获取当前认证用户信息
     * @return 当前用户对象
     */
    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String username = authentication.getName();
        return userDao.findByUsername(username);
    }
}