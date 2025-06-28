package com.edu.virtuallab.auth.service;

import com.edu.virtuallab.auth.model.User;
import com.edu.virtuallab.common.api.CommonResult;
import java.util.Map;

public interface AuthService {
    CommonResult<Map<String, Object>> loginWithPassword(String username, String password);
    CommonResult<Boolean> loginWithSms(String phone, String code);
    CommonResult<Boolean> loginWithFingerprint(String username, String fingerprintData);
    CommonResult<Boolean> sendSmsCode(String phone);
    User getCurrentUser();
} 