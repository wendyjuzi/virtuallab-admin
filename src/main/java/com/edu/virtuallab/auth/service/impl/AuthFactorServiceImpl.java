package com.edu.virtuallab.auth.service.impl;

import com.edu.virtuallab.auth.dao.AuthFactorDao;
import com.edu.virtuallab.auth.model.AuthFactor;
import com.edu.virtuallab.auth.service.AuthFactorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class AuthFactorServiceImpl implements AuthFactorService {
    
    @Autowired
    private AuthFactorDao authFactorDao;
    
    @Override
    public List<AuthFactor> findByUserId(Long userId) {
        return authFactorDao.findByUserId(userId);
    }
    
    @Override
    public AuthFactor findByUserIdAndType(Long userId, String factorType) {
        return authFactorDao.findByUserIdAndType(userId, factorType);
    }
    
    @Override
    public boolean addAuthFactor(AuthFactor authFactor) {
        authFactor.setCreateTime(new Date());
        authFactor.setUpdateTime(new Date());
        return authFactorDao.insert(authFactor) > 0;
    }
    
    @Override
    public boolean updateAuthFactor(AuthFactor authFactor) {
        authFactor.setUpdateTime(new Date());
        return authFactorDao.update(authFactor) > 0;
    }
    
    @Override
    public boolean deleteAuthFactor(Long id) {
        return authFactorDao.deleteById(id) > 0;
    }
    
    @Override
    public boolean deleteByUserId(Long userId) {
        return authFactorDao.deleteByUserId(userId) > 0;
    }
    
    @Override
    public boolean sendSmsCode(Long userId, String phone) {
        // 生成6位随机验证码
        String code = generateRandomCode(6);
        
        // 设置过期时间为5分钟后
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        Date expireTime = calendar.getTime();
        
        // 保存或更新验证码
        AuthFactor authFactor = authFactorDao.findByUserIdAndType(userId, AuthFactor.TYPE_SMS);
        if (authFactor == null) {
            authFactor = new AuthFactor();
            authFactor.setUserId(userId);
            authFactor.setFactorType(AuthFactor.TYPE_SMS);
            authFactor.setFactorValue(phone);
            authFactor.setStatus(1);
            authFactor.setIsDefault(0);
        }
        
        authFactor.setFactorCode(code);
        authFactor.setExpireTime(expireTime);
        authFactor.setUpdateTime(new Date());
        
        // TODO: 调用短信服务发送验证码
        System.out.println("发送短信验证码到 " + phone + ": " + code);
        
        return authFactorDao.update(authFactor) > 0 || authFactorDao.insert(authFactor) > 0;
    }
    
    @Override
    public boolean sendEmailCode(Long userId, String email) {
        // 生成6位随机验证码
        String code = generateRandomCode(6);
        
        // 设置过期时间为10分钟后
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        Date expireTime = calendar.getTime();
        
        // 保存或更新验证码
        AuthFactor authFactor = authFactorDao.findByUserIdAndType(userId, AuthFactor.TYPE_EMAIL);
        if (authFactor == null) {
            authFactor = new AuthFactor();
            authFactor.setUserId(userId);
            authFactor.setFactorType(AuthFactor.TYPE_EMAIL);
            authFactor.setFactorValue(email);
            authFactor.setStatus(1);
            authFactor.setIsDefault(0);
        }
        
        authFactor.setFactorCode(code);
        authFactor.setExpireTime(expireTime);
        authFactor.setUpdateTime(new Date());
        
        // TODO: 调用邮件服务发送验证码
        System.out.println("发送邮箱验证码到 " + email + ": " + code);
        
        return authFactorDao.update(authFactor) > 0 || authFactorDao.insert(authFactor) > 0;
    }
    
    @Override
    public boolean validateSmsCode(Long userId, String phone, String code) {
        AuthFactor authFactor = authFactorDao.validateSmsCode(userId, phone, code);
        if (authFactor != null && authFactor.getExpireTime().after(new Date())) {
            // 验证成功后删除验证码
            authFactorDao.deleteById(authFactor.getId());
            return true;
        }
        return false;
    }
    
    @Override
    public boolean validateEmailCode(Long userId, String email, String code) {
        AuthFactor authFactor = authFactorDao.validateEmailCode(userId, email, code);
        if (authFactor != null && authFactor.getExpireTime().after(new Date())) {
            // 验证成功后删除验证码
            authFactorDao.deleteById(authFactor.getId());
            return true;
        }
        return false;
    }
    
    @Override
    public boolean validateFingerprint(Long userId, String fingerprint) {
        AuthFactor authFactor = authFactorDao.validateFingerprint(userId, fingerprint);
        return authFactor != null && authFactor.getStatus() == 1;
    }
    
    @Override
    public boolean setDefaultAuthFactor(Long userId, String factorType) {
        // 先取消所有默认认证方式
        List<AuthFactor> authFactors = authFactorDao.findByUserId(userId);
        for (AuthFactor factor : authFactors) {
            if (factor.getIsDefault() == 1) {
                factor.setIsDefault(0);
                authFactorDao.update(factor);
            }
        }
        
        // 设置新的默认认证方式
        AuthFactor authFactor = authFactorDao.findByUserIdAndType(userId, factorType);
        if (authFactor != null) {
            authFactor.setIsDefault(1);
            authFactor.setUpdateTime(new Date());
            return authFactorDao.update(authFactor) > 0;
        }
        return false;
    }
    
    @Override
    public boolean hasMultiFactorAuth(Long userId) {
        List<AuthFactor> authFactors = authFactorDao.findByUserId(userId);
        return authFactors.size() > 1;
    }
    
    // 生成随机验证码
    private String generateRandomCode(int length) {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
} 