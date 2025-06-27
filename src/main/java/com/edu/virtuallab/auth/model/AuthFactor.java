package com.edu.virtuallab.auth.model;

import java.util.Date;

public class AuthFactor {
    private Long id;
    private Long userId;
    private String factorType; // 认证类型：password-密码 sms-短信 email-邮箱 fingerprint-指纹
    private String factorValue; // 认证值
    private String factorCode; // 验证码（用于短信、邮箱验证）
    private Date expireTime; // 过期时间
    private Integer status; // 0-禁用 1-启用
    private Integer isDefault; // 是否默认认证方式：0-否 1-是
    private Date createTime;
    private Date updateTime;

    // 认证类型常量
    public static final String TYPE_PASSWORD = "password";
    public static final String TYPE_SMS = "sms";
    public static final String TYPE_EMAIL = "email";
    public static final String TYPE_FINGERPRINT = "fingerprint";

    // getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getFactorType() { return factorType; }
    public void setFactorType(String factorType) { this.factorType = factorType; }
    public String getFactorValue() { return factorValue; }
    public void setFactorValue(String factorValue) { this.factorValue = factorValue; }
    public String getFactorCode() { return factorCode; }
    public void setFactorCode(String factorCode) { this.factorCode = factorCode; }
    public Date getExpireTime() { return expireTime; }
    public void setExpireTime(Date expireTime) { this.expireTime = expireTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getIsDefault() { return isDefault; }
    public void setIsDefault(Integer isDefault) { this.isDefault = isDefault; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
} 