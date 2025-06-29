package com.edu.virtuallab.auth.controller;

import com.edu.virtuallab.auth.model.AuthFactor;
import com.edu.virtuallab.auth.model.EmailSendRequest;
import com.edu.virtuallab.auth.service.AuthFactorService;
import com.edu.virtuallab.auth.service.EmailVerificationService;
import com.edu.virtuallab.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/factor")
public class AuthFactorController {
    
    @Autowired
    private AuthFactorService authFactorService;
    
    @Autowired
    private EmailVerificationService emailVerificationService;
    
    // 获取用户的认证因素列表
    @GetMapping("/user/{userId}")
    public CommonResult<List<AuthFactor>> getUserAuthFactors(@PathVariable Long userId) {
        List<AuthFactor> authFactors = authFactorService.findByUserId(userId);
        return CommonResult.success(authFactors);
    }
    
    // 添加认证因素
    @PostMapping("/add")
    public CommonResult<Boolean> addAuthFactor(@RequestBody AuthFactor authFactor) {
        boolean result = authFactorService.addAuthFactor(authFactor);
        return CommonResult.success(result);
    }
    
    // 更新认证因素
    @PutMapping("/update")
    public CommonResult<Boolean> updateAuthFactor(@RequestBody AuthFactor authFactor) {
        boolean result = authFactorService.updateAuthFactor(authFactor);
        return CommonResult.success(result);
    }
    
    // 删除认证因素
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> deleteAuthFactor(@PathVariable Long id) {
        boolean result = authFactorService.deleteAuthFactor(id);
        return CommonResult.success(result);
    }
    
    // 发送短信验证码（注册时使用，不需要userId）
    @PostMapping("/sms/send/register")
    public CommonResult<Boolean> sendSmsCodeForRegister(@RequestParam String phone, @RequestParam(required = false) String userType) {
        if (userType == null || !"admin".equalsIgnoreCase(userType)) {
            return CommonResult.failed("只有管理员注册时才允许发送短信验证码");
        }
        boolean result = authFactorService.sendSmsCodeForRegister(phone);
        return CommonResult.success(result);
    }
    
    // 发送短信验证码（已登录用户使用）
    @PostMapping("/sms/send")
    public CommonResult<Boolean> sendSmsCode(@RequestParam(required = false) Long userId, @RequestParam String phone, @RequestParam(required = false) String userType) {
        if (userType == null || !"admin".equalsIgnoreCase(userType)) {
            return CommonResult.failed("只有管理员注册时才允许发送短信验证码");
        }
        boolean result = authFactorService.sendSmsCode(userId, phone);
        return CommonResult.success(result);
    }
    
    // 验证短信验证码（注册时使用，不需要userId）
    @PostMapping("/sms/validate/register")
    public CommonResult<Boolean> validateSmsCodeForRegister(@RequestParam String phone, @RequestParam String code) {
        boolean result = authFactorService.validateSmsCodeForRegister(phone, code);
        return CommonResult.success(result);
    }
    
    // 验证短信验证码（已登录用户使用）
    @PostMapping("/sms/validate")
    public CommonResult<Boolean> validateSmsCode(@RequestParam(required = false) Long userId, 
                                                @RequestParam String phone, 
                                                @RequestParam String code) {
        boolean result = authFactorService.validateSmsCode(userId, phone, code);
        return CommonResult.success(result);
    }
    
    // 发送邮箱验证码（注册时使用，不需要userId）
    @PostMapping("/email/send/register")
    public CommonResult<Boolean> sendEmailCodeForRegister(@RequestParam String email, @RequestParam(required = false) String userType) {
        if (userType == null || !"admin".equalsIgnoreCase(userType)) {
            return CommonResult.failed("只有管理员注册时才允许发送邮箱验证码");
        }
        boolean result = authFactorService.sendEmailCodeForRegister(email);
        return CommonResult.success(result);
    }
    
    // 发送邮箱验证码（已登录用户使用）
    @PostMapping("/email/send")
    public CommonResult<Boolean> sendEmailCode(@RequestParam(required = false) Long userId, @RequestParam String email, @RequestParam(required = false) String userType) {
        if (userType == null || !"admin".equalsIgnoreCase(userType)) {
            return CommonResult.failed("只有管理员注册时才允许发送邮箱验证码");
        }
        boolean result = authFactorService.sendEmailCode(userId, email);
        return CommonResult.success(result);
    }
    
    // 验证邮箱验证码（注册时使用，不需要userId）
    @PostMapping("/email/validate/register")
    public CommonResult<Boolean> validateEmailCodeForRegister(@RequestParam String email, @RequestParam String code) {
        boolean result = authFactorService.validateEmailCodeForRegister(email, code);
        return CommonResult.success(result);
    }
    
    // 验证邮箱验证码（已登录用户使用）
    @PostMapping("/email/validate")
    public CommonResult<Boolean> validateEmailCode(@RequestParam(required = false) Long userId, 
                                                  @RequestParam String email, 
                                                  @RequestParam String code) {
        boolean result = authFactorService.validateEmailCode(userId, email, code);
        return CommonResult.success(result);
    }
    
    // 验证指纹
    @PostMapping("/fingerprint/validate")
    public CommonResult<Boolean> validateFingerprint(@RequestParam Long userId, 
                                                    @RequestParam String fingerprint) {
        boolean result = authFactorService.validateFingerprint(userId, fingerprint);
        return CommonResult.success(result);
    }
    
    // 设置默认认证方式
    @PostMapping("/default")
    public CommonResult<Boolean> setDefaultAuthFactor(@RequestParam Long userId, 
                                                     @RequestParam String factorType) {
        boolean result = authFactorService.setDefaultAuthFactor(userId, factorType);
        return CommonResult.success(result);
    }
    
    // 检查是否启用多因素认证
    @GetMapping("/multi-factor/{userId}")
    public CommonResult<Boolean> hasMultiFactorAuth(@PathVariable Long userId) {
        boolean result = authFactorService.hasMultiFactorAuth(userId);
        return CommonResult.success(result);
    }

    // 通用邮箱验证码发送接口（支持所有用户类型）
    @PostMapping("/email/send-code")
    public CommonResult<Boolean> sendEmailCodeForAll(@RequestParam String email, @RequestParam(required = false) String type) {
        // 如果没有指定类型，默认为LOGIN
        if (type == null || type.trim().isEmpty()) {
            type = "LOGIN";
        }
        
        // 调用邮箱验证服务发送验证码
        return emailVerificationService.sendVerificationCode(email, type);
    }

    // 支持JSON格式的邮箱验证码发送接口
    @PostMapping("/email/send-code-json")
    public CommonResult<Boolean> sendEmailCodeJson(@RequestBody EmailSendRequest request) {
        try {
            String email = request.getEmail();
            String type = request.getType();
            
            if (email == null || email.trim().isEmpty()) {
                return CommonResult.failed("邮箱地址不能为空");
            }
            
            // 如果没有指定类型，默认为LOGIN
            if (type == null || type.trim().isEmpty()) {
                type = "LOGIN";
            }
            
            // 调用邮箱验证服务发送验证码
            return emailVerificationService.sendVerificationCode(email, type);
        } catch (Exception e) {
            return CommonResult.failed("发送失败: " + e.getMessage());
        }
    }

    // 支持多种格式的邮箱验证码发送接口
    @PostMapping(value = {"/email/send-flexible", "/email/send-code-flexible"})
    public CommonResult<Boolean> sendEmailCodeFlexible(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String type,
            @RequestBody(required = false) EmailSendRequest requestBody) {
        
        try {
            String finalEmail = null;
            String finalType = "LOGIN"; // 默认类型
            
            // 优先从请求体获取参数
            if (requestBody != null) {
                finalEmail = requestBody.getEmail();
                if (requestBody.getType() != null && !requestBody.getType().trim().isEmpty()) {
                    finalType = requestBody.getType();
                }
            }
            
            // 如果请求体没有，则从请求参数获取
            if (finalEmail == null || finalEmail.trim().isEmpty()) {
                finalEmail = email;
            }
            
            if (type != null && !type.trim().isEmpty()) {
                finalType = type;
            }
            
            // 验证邮箱地址
            if (finalEmail == null || finalEmail.trim().isEmpty()) {
                return CommonResult.failed("邮箱地址不能为空");
            }
            
            // 调用邮箱验证服务发送验证码
            return emailVerificationService.sendVerificationCode(finalEmail, finalType);
        } catch (Exception e) {
            return CommonResult.failed("发送失败: " + e.getMessage());
        }
    }

    // 测试接口 - 用于验证API是否正常工作
    @GetMapping("/test")
    public CommonResult<String> test() {
        return CommonResult.success("API服务正常运行");
    }

    // 测试邮箱发送接口 - GET方法（用于浏览器直接访问测试）
    @GetMapping("/email/test")
    public CommonResult<String> testEmailSend(@RequestParam String email) {
        return CommonResult.success("邮箱测试接口正常，邮箱地址: " + email);
    }
} 