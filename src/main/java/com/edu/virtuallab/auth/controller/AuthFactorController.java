package com.edu.virtuallab.auth.controller;

import com.edu.virtuallab.auth.model.AuthFactor;
import com.edu.virtuallab.auth.service.AuthFactorService;
import com.edu.virtuallab.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/factor")
public class AuthFactorController {
    
    @Autowired
    private AuthFactorService authFactorService;
    
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
    
    // 发送短信验证码
    @PostMapping("/sms/send")
    public CommonResult<Boolean> sendSmsCode(@RequestParam Long userId, @RequestParam String phone) {
        boolean result = authFactorService.sendSmsCode(userId, phone);
        return CommonResult.success(result);
    }
    
    // 验证短信验证码
    @PostMapping("/sms/validate")
    public CommonResult<Boolean> validateSmsCode(@RequestParam Long userId, 
                                                @RequestParam String phone, 
                                                @RequestParam String code) {
        boolean result = authFactorService.validateSmsCode(userId, phone, code);
        return CommonResult.success(result);
    }
    
    // 发送邮箱验证码
    @PostMapping("/email/send")
    public CommonResult<Boolean> sendEmailCode(@RequestParam Long userId, @RequestParam String email) {
        boolean result = authFactorService.sendEmailCode(userId, email);
        return CommonResult.success(result);
    }
    
    // 验证邮箱验证码
    @PostMapping("/email/validate")
    public CommonResult<Boolean> validateEmailCode(@RequestParam Long userId, 
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
} 