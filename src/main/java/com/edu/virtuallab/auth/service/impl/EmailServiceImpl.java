package com.edu.virtuallab.auth.service.impl;

import com.edu.virtuallab.auth.service.EmailService;
import com.edu.virtuallab.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * 邮件服务实现类
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = Logger.getLogger(EmailServiceImpl.class.getName());

    public static final String TYPE_REGISTER = "REGISTER";
    public static final String TYPE_RESET_PASSWORD = "RESET_PASSWORD";
    public static final String TYPE_LOGIN = "LOGIN";
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String from;

    @Override
    public CommonResult<Boolean> sendVerificationCode(String to, String code, String type) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);

            String subject = getSubjectByType(type);
            String content = getVerificationCodeContent(code, type);

            message.setSubject(subject);
            message.setText(content);

            mailSender.send(message);
            LOGGER.info("验证码邮件发送成功 -> " + to);
            return CommonResult.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.severe("验证码邮件发送失败: " + e.getMessage());
            return CommonResult.failed("邮件发送失败: " + e.getMessage());
        }
    }

    @Override
    public CommonResult<Boolean> sendRegistrationSuccess(String to, String username) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject("虚拟仿真实训教学管理平台 - 注册成功");
            message.setText(String.format(
                    "亲爱的 %s，\n\n" +
                            "恭喜您成功注册虚拟仿真实训教学管理平台！\n\n" +
                            "您的账户已经激活，现在可以登录使用平台的所有功能。\n\n" +
                            "如有任何问题，请联系管理员。\n\n" +
                            "祝您使用愉快！\n\n" +
                            "虚拟仿真实训教学管理平台团队",
                    username
            ));

            mailSender.send(message);
            LOGGER.info("注册成功邮件发送成功 -> " + to);
            return CommonResult.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.severe("注册成功邮件发送失败: " + e.getMessage());
            return CommonResult.failed("邮件发送失败: " + e.getMessage());
        }
    }

    @Override
    public CommonResult<Boolean> sendPasswordReset(String to, String resetLink) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject("虚拟仿真实训教学管理平台 - 密码重置");
            message.setText(String.format(
                    "亲爱的用户，\n\n" +
                            "您请求重置密码。请点击以下链接重置您的密码：\n\n" +
                            "%s\n\n" +
                            "此链接将在24小时后失效。\n\n" +
                            "如果您没有请求重置密码，请忽略此邮件。\n\n" +
                            "虚拟仿真实训教学管理平台团队",
                    resetLink
            ));

            mailSender.send(message);
            LOGGER.info("密码重置邮件发送成功 -> " + to);
            return CommonResult.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.severe("密码重置邮件发送失败: " + e.getMessage());
            return CommonResult.failed("邮件发送失败: " + e.getMessage());
        }
    }

    /**
     * 根据验证码类型获取邮件主题
     */
    private String getSubjectByType(String type) {
        switch (type) {
            case TYPE_REGISTER:
                return "虚拟仿真实训教学管理平台 - 注册验证码";
            case TYPE_RESET_PASSWORD:
                return "虚拟仿真实训教学管理平台 - 密码重置验证码";
            case TYPE_LOGIN:
                return "虚拟仿真实训教学管理平台 - 登录验证码";
            default:
                return "虚拟仿真实训教学管理平台 - 验证码";
        }
    }

    /**
     * 根据验证码类型获取邮件内容
     */
    private String getVerificationCodeContent(String code, String type) {
        String action;
        switch (type) {
            case TYPE_REGISTER:
                action = "注册";
                break;
            case TYPE_RESET_PASSWORD:
                action = "重置密码";
                break;
            case TYPE_LOGIN:
                action = "登录";
                break;
            default:
                action = "验证";
        }

        return String.format(
                "亲爱的用户，\n\n" +
                        "您的%s验证码是：%s\n\n" +
                        "验证码有效期为10分钟，请尽快使用。\n\n" +
                        "如果您没有请求%s，请忽略此邮件。\n\n" +
                        "虚拟仿真实训教学管理平台团队",
                action, code, action
        );
    }
}
