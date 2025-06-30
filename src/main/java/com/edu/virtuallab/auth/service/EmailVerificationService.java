package com.edu.virtuallab.auth.service;

import com.edu.virtuallab.common.api.CommonResult;

/**
 * 邮箱验证服务接口
 */
public interface EmailVerificationService {
    
    /**
     * 发送邮箱验证码
     * @param email 邮箱地址
     * @param type 验证码类型
     * @return 发送结果
     */
    CommonResult<Boolean> sendVerificationCode(String email, String type);
    
    /**
     * 验证邮箱验证码
     * @param email 邮箱地址
     * @param code 验证码
     * @param type 验证码类型
     * @return 验证结果
     */
    CommonResult<Boolean> verifyCode(String email, String code, String type);
    
    /**
     * 检查邮箱是否已验证
     * @param email 邮箱地址
     * @return 是否已验证
     */
    boolean isEmailVerified(String email);
} 