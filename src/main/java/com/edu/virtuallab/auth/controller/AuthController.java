package com.edu.virtuallab.auth.controller;

import com.edu.virtuallab.auth.model.User;
import com.edu.virtuallab.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login/password")
    public boolean loginWithPassword(@RequestParam String username, @RequestParam String password) {
        return authService.loginWithPassword(username, password);
    }

    @PostMapping("/login/sms")
    public boolean loginWithSms(@RequestParam String phone, @RequestParam String code) {
        return authService.loginWithSms(phone, code);
    }

    @PostMapping("/login/fingerprint")
    public boolean loginWithFingerprint(@RequestParam String username, @RequestParam String fingerprintData) {
        return authService.loginWithFingerprint(username, fingerprintData);
    }

    @PostMapping("/sendSmsCode")
    public boolean sendSmsCode(@RequestParam String phone) {
        return authService.sendSmsCode(phone);
    }

    @GetMapping("/currentUser")
    public User getCurrentUser() {
        return authService.getCurrentUser();
    }
} 