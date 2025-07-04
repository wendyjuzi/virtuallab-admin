package com.edu.virtuallab.auth.dao;

import com.edu.virtuallab.auth.model.AuthFactor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuthFactorDao {
    
    // 根据用户ID查询认证因素
    List<AuthFactor> findByUserId(@Param("userId") Long userId);
    
    // 根据用户ID和认证类型查询
    AuthFactor findByUserIdAndType(@Param("userId") Long userId, @Param("factorType") String factorType);
    
    // 插入认证因素
    int insert(AuthFactor authFactor);
    
    // 更新认证因素
    int update(AuthFactor authFactor);
    
    // 删除认证因素
    int deleteById(@Param("id") Long id);
    
    // 根据用户ID删除所有认证因素
    int deleteByUserId(@Param("userId") Long userId);
    
    // 验证邮箱验证码
    AuthFactor validateEmailCode(@Param("userId") Long userId, @Param("email") String email, @Param("code") String code);
    
    // 验证指纹
    AuthFactor validateFingerprint(@Param("userId") Long userId, @Param("fingerprint") String fingerprint);
} 