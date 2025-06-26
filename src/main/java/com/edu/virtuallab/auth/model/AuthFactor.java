package com.edu.virtuallab.auth.model;

import java.util.Date;

public class AuthFactor {
    private Long id;
    private Long userId;
    private String type; // password/sms/fingerprint
    private String value;
    private Integer status;
    private Date createTime;
    // getter/setter
} 