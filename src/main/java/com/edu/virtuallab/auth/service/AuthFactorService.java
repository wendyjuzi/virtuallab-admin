package com.edu.virtuallab.auth.service;

import com.edu.virtuallab.auth.model.AuthFactor;

import java.util.List;

public interface AuthFactorService {
    
    // 根据用户ID查询认证因素
    List<AuthFactor> findByUserId(Long userId);
    
    // 根据用户ID和认证类型查询
    AuthFactor findByUserIdAndType(Long userId, String factorType);
    
    // 添加认证因素
    boolean addAuthFactor(AuthFactor authFactor);
    
    // 更新认证因素
    boolean updateAuthFactor(AuthFactor authFactor);
    
    // 删除认证因素
    boolean deleteAuthFactor(Long id);
    
    // 根据用户ID删除所有认证因素
    boolean deleteByUserId(Long userId);
    
    // 发送邮箱验证码
    boolean sendEmailCode(Long userId, String email);
    
    // 发送邮箱验证码（注册时使用，不需要userId）
    boolean sendEmailCodeForRegister(String email);
    
    // 验证邮箱验证码
    boolean validateEmailCode(Long userId, String email, String code);
    
    // 验证邮箱验证码（注册时使用，不需要userId）
    boolean validateEmailCodeForRegister(String email, String code);
    
    // 验证指纹
    boolean validateFingerprint(Long userId, String fingerprint);
    
    // 设置默认认证方式
    boolean setDefaultAuthFactor(Long userId, String factorType);
    
    // 检查用户是否启用了多因素认证
    boolean hasMultiFactorAuth(Long userId);
} 