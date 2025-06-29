package com.edu.virtuallab.auth.dao;

import com.edu.virtuallab.auth.model.EmailVerificationCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 邮箱验证码数据访问接口
 */
@Mapper
public interface EmailVerificationCodeDao {
    
    /**
     * 插入验证码记录
     */
    int insert(EmailVerificationCode code);
    
    /**
     * 根据邮箱和类型查询最新的验证码
     */
    EmailVerificationCode findLatestByEmailAndType(@Param("email") String email, @Param("type") String type);
    
    /**
     * 根据邮箱查询所有验证码
     */
    List<EmailVerificationCode> findByEmail(@Param("email") String email);
    
    /**
     * 标记验证码为已使用
     */
    int markAsUsed(@Param("id") Long id);
    
    /**
     * 删除过期的验证码
     */
    int deleteExpiredCodes();
    
    /**
     * 根据ID查询验证码
     */
    EmailVerificationCode findById(@Param("id") Long id);
} 