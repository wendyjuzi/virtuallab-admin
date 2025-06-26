package com.edu.virtuallab.auth.service;

import com.edu.virtuallab.auth.model.User;

public interface AuthService {
    boolean loginWithPassword(String username, String password);
    boolean loginWithSms(String phone, String code);
    boolean loginWithFingerprint(String username, String fingerprintData);
    boolean sendSmsCode(String phone);
    User getCurrentUser();
} 