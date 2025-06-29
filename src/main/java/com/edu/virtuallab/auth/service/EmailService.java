package com.edu.virtuallab.auth.service;

import com.edu.virtuallab.common.api.CommonResult;

/**
 * 邮件服务接口
 */
public interface EmailService {
    
    /**
     * 发送验证码邮件
     * @param to 收件人邮箱
     * @param code 验证码
     * @param type 验证码类型
     * @return 发送结果
     */
    CommonResult<Boolean> sendVerificationCode(String to, String code, String type);
    
    /**
     * 发送注册成功邮件
     * @param to 收件人邮箱
     * @param username 用户名
     * @return 发送结果
     */
    CommonResult<Boolean> sendRegistrationSuccess(String to, String username);
    
    /**
     * 发送密码重置邮件
     * @param to 收件人邮箱
     * @param resetLink 重置链接
     * @return 发送结果
     */
    CommonResult<Boolean> sendPasswordReset(String to, String resetLink);
} 