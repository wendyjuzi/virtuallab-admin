package com.edu.virtuallab.auth.model;

import java.util.Date;

/**
 * 邮箱验证码模型
 */
public class EmailVerificationCode {
    private Long id;
    private String email;
    private String code;
    private Date createTime;
    private Date expireTime;
    private Boolean used;
    private String type; // 验证码类型：REGISTER-注册, RESET_PASSWORD-重置密码, LOGIN-登录

    // 验证码类型常量
    public static final String TYPE_REGISTER = "REGISTER";
    public static final String TYPE_RESET_PASSWORD = "RESET_PASSWORD";
    public static final String TYPE_LOGIN = "LOGIN";

    // 验证码有效期（分钟）
    public static final int EXPIRE_MINUTES = 10;

    public EmailVerificationCode() {}

    public EmailVerificationCode(String email, String code, String type) {
        this.email = email;
        this.code = code;
        this.type = type;
        this.createTime = new Date();
        this.expireTime = new Date(System.currentTimeMillis() + EXPIRE_MINUTES * 60 * 1000L);
        this.used = false;
    }

    // getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getExpireTime() { return expireTime; }
    public void setExpireTime(Date expireTime) { this.expireTime = expireTime; }
    public Boolean getUsed() { return used; }
    public void setUsed(Boolean used) { this.used = used; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    /**
     * 检查验证码是否过期
     */
    public boolean isExpired() {
        return new Date().after(expireTime);
    }

    /**
     * 检查验证码是否已使用（使用getUsed()方法）
     */
    public boolean isCodeUsed() {
        return used != null && used;
    }
} 