package com.edu.virtuallab.auth.model;

/**
 * 邮箱发送请求模型
 */
public class EmailSendRequest {
    private String email;
    private String type;

    public EmailSendRequest() {}

    public EmailSendRequest(String email, String type) {
        this.email = email;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "EmailSendRequest{" +
                "email='" + email + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
} 