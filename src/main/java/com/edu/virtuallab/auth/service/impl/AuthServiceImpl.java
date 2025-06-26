package com.edu.virtuallab.auth.service.impl;

import com.edu.virtuallab.auth.model.User;
import com.edu.virtuallab.auth.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public boolean loginWithPassword(String username, String password) {
        // TODO: 实现登录逻辑
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean loginWithSms(String phone, String code) {
        // TODO: 实现短信登录逻辑
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean loginWithFingerprint(String username, String fingerprintData) {
        // TODO: 实现指纹登录逻辑
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean sendSmsCode(String phone) {
        // TODO: 实现发送短信验证码逻辑
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public User getCurrentUser() {
        // TODO: 实现获取当前用户逻辑
        throw new UnsupportedOperationException("Not implemented yet");
    }
} 