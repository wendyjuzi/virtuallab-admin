package com.edu.virtuallab.auth.controller;

import com.edu.virtuallab.auth.service.AuthService;
import com.edu.virtuallab.auth.service.EmailVerificationService;
import com.edu.virtuallab.common.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 邮箱验证控制器
 * 统一的邮箱验证码管理接口
 */
@RestController
@RequestMapping("/auth/email")
@Api(tags = "邮箱验证管理")
public class EmailVerificationController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    // ==================== 请求体DTO类 ====================
    
    /**
     * 发送验证码请求体
     */
    public static class SendCodeRequest {
        private String email;
        private String type;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    /**
     * 邮箱登录请求体
     */
    public static class EmailLoginRequest {
        private String email;
        private String code;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
    }

    /**
     * 验证码验证请求体
     */
    public static class VerifyCodeRequest {
        private String email;
        private String code;
        private String type;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    // ==================== 邮箱验证码接口 ====================

    @ApiOperation("发送邮箱验证码")
    @PostMapping("/send-code")
    public CommonResult<Boolean> sendVerificationCode(@RequestBody SendCodeRequest request) {
        try {
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return CommonResult.failed("邮箱地址不能为空");
            }
            
            // 如果没有指定类型，默认为LOGIN
            String type = request.getType();
            if (type == null || type.trim().isEmpty()) {
                type = "LOGIN";
            }
            
            return authService.sendEmailCode(request.getEmail(), type);
        } catch (Exception e) {
            return CommonResult.failed("发送失败: " + e.getMessage());
        }
    }

    @ApiOperation("邮箱验证码登录")
    @PostMapping("/login")
    public CommonResult<Map<String, Object>> loginWithEmail(@RequestBody EmailLoginRequest request) {
        try {
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return CommonResult.failed("邮箱地址不能为空");
            }
            if (request.getCode() == null || request.getCode().trim().isEmpty()) {
                return CommonResult.failed("验证码不能为空");
            }
            
            return authService.loginWithEmail(request.getEmail(), request.getCode());
        } catch (Exception e) {
            return CommonResult.failed("登录失败: " + e.getMessage());
        }
    }

    @ApiOperation("验证邮箱验证码")
    @PostMapping("/verify")
    public CommonResult<Boolean> verifyCode(@RequestBody VerifyCodeRequest request) {
        try {
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return CommonResult.failed("邮箱地址不能为空");
            }
            if (request.getCode() == null || request.getCode().trim().isEmpty()) {
                return CommonResult.failed("验证码不能为空");
            }
            if (request.getType() == null || request.getType().trim().isEmpty()) {
                return CommonResult.failed("验证码类型不能为空");
            }
            
            return emailVerificationService.verifyCode(request.getEmail(), request.getCode(), request.getType());
        } catch (Exception e) {
            return CommonResult.failed("验证失败: " + e.getMessage());
        }
    }

    @ApiOperation("检查邮箱是否已验证")
    @GetMapping("/check")
    public CommonResult<Boolean> checkEmailVerified(@RequestParam String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                return CommonResult.failed("邮箱地址不能为空");
            }
            
            boolean verified = emailVerificationService.isEmailVerified(email);
            return CommonResult.success(verified, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("检查失败: " + e.getMessage());
        }
    }

    // ==================== 兼容性接口（保持向后兼容） ====================
    
    @ApiOperation("发送邮箱验证码（兼容旧接口）")
    @PostMapping("/send-code-compat")
    public CommonResult<Boolean> sendVerificationCodeCompat(@RequestParam String email, 
                                                           @RequestParam(required = false) String type) {
        SendCodeRequest request = new SendCodeRequest();
        request.setEmail(email);
        request.setType(type);
        return sendVerificationCode(request);
    }

    @ApiOperation("邮箱验证码登录（兼容旧接口）")
    @PostMapping("/login-compat")
    public CommonResult<Map<String, Object>> loginWithEmailCompat(@RequestParam String email, 
                                                                 @RequestParam String code) {
        EmailLoginRequest request = new EmailLoginRequest();
        request.setEmail(email);
        request.setCode(code);
        return loginWithEmail(request);
    }

    @ApiOperation("验证邮箱验证码（兼容旧接口）")
    @PostMapping("/verify-compat")
    public CommonResult<Boolean> verifyCodeCompat(@RequestParam String email, 
                                                @RequestParam String code, 
                                                @RequestParam String type) {
        VerifyCodeRequest request = new VerifyCodeRequest();
        request.setEmail(email);
        request.setCode(code);
        request.setType(type);
        return verifyCode(request);
    }
}
