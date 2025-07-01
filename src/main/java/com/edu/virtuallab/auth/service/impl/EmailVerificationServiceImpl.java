package com.edu.virtuallab.auth.service.impl;

import com.edu.virtuallab.auth.dao.EmailVerificationCodeDao;
import com.edu.virtuallab.auth.model.EmailVerificationCode;
import com.edu.virtuallab.auth.service.EmailService;
import com.edu.virtuallab.auth.service.EmailVerificationService;
import com.edu.virtuallab.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * 邮箱验证服务实现类
 */
@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

    @Autowired
    private EmailVerificationCodeDao emailVerificationCodeDao;
    
    @Autowired
    private EmailService emailService;

    @Override
    public CommonResult<Boolean> sendVerificationCode(String email, String type) {
        try {
            // 生成6位数字验证码
            String code = generateVerificationCode();
            
            // 创建验证码记录
            EmailVerificationCode verificationCode = new EmailVerificationCode(email, code, type);
            
            // 保存到数据库
            emailVerificationCodeDao.insert(verificationCode);
            
            // 发送邮件
            CommonResult<Boolean> sendResult = emailService.sendVerificationCode(email, code, type);
            
            if (sendResult.isSuccess()) {
                return CommonResult.success(true, "资源更新成功");
            } else {
                return CommonResult.failed("邮件发送失败: " + sendResult.getMessage());
            }
        } catch (Exception e) {
            return CommonResult.failed("发送验证码失败: " + e.getMessage());
        }
    }

    @Override
    public CommonResult<Boolean> verifyCode(String email, String code, String type) {
        try {
            // 查询最新的验证码
            EmailVerificationCode verificationCode = emailVerificationCodeDao.findLatestByEmailAndType(email, type);
            
            if (verificationCode == null) {
                return CommonResult.failed("验证码不存在");
            }
            
            // 检查验证码是否已使用
            if (verificationCode.isCodeUsed()) {
                return CommonResult.failed("验证码已使用");
            }
            
            // 检查验证码是否过期
            if (verificationCode.isExpired()) {
                return CommonResult.failed("验证码已过期");
            }
            
            // 检查验证码是否正确
            if (!code.equals(verificationCode.getCode())) {
                return CommonResult.failed("验证码错误");
            }
            
            // 标记验证码为已使用
            emailVerificationCodeDao.markAsUsed(verificationCode.getId());
            
            return CommonResult.success(true, "资源更新成功");
        } catch (Exception e) {
            return CommonResult.failed("验证失败: " + e.getMessage());
        }
    }

    @Override
    public boolean isEmailVerified(String email) {
        try {
            // 检查是否有有效的验证码记录
            EmailVerificationCode verificationCode = emailVerificationCodeDao.findLatestByEmailAndType(email, EmailVerificationCode.TYPE_REGISTER);
            return verificationCode != null && !verificationCode.isExpired();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean verify(String email, String code) {
        // 正确写法：先获取List，再取第一个
        List<EmailVerificationCode> codeList = emailVerificationCodeDao.findByEmail(email);
        if (codeList == null || codeList.isEmpty()) return false;
        EmailVerificationCode entity = codeList.get(0); // 取最新或第一个
        return entity.getCode().equals(code) && !entity.isExpired();
    }
    /**
     * 生成6位数字验证码
     */
    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
} 