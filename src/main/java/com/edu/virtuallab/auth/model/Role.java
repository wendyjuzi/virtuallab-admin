package com.edu.virtuallab.auth.model;

import java.util.Date;

public class Role {
    private Long id;
    private String name;
    private String code; // 角色编码
    private String description;
    private Integer type; // 角色类型：1-系统角色 2-自定义角色
    private Integer level; // 权限级别：1-系统管理员 2-院系管理员 3-教师 4-学生 5-访客
    private Integer status; // 0-禁用 1-启用
    private Long parentId; // 父角色ID，用于角色继承
    private Date createTime;
    private Date updateTime;

    // 角色类型常量
    public static final int TYPE_SYSTEM = 1;
    public static final int TYPE_CUSTOM = 2;

    // 角色级别常量
    public static final int LEVEL_SYSTEM_ADMIN = 1;
    public static final int LEVEL_DEPARTMENT_ADMIN = 2;
    public static final int LEVEL_TEACHER = 3;
    public static final int LEVEL_STUDENT = 4;
    public static final int LEVEL_GUEST = 5;

    // 系统角色编码常量
    public static final String CODE_SYSTEM_ADMIN = "SYSTEM_ADMIN";
    public static final String CODE_DEPARTMENT_ADMIN = "DEPARTMENT_ADMIN";
    public static final String CODE_TEACHER = "TEACHER";
    public static final String CODE_STUDENT = "STUDENT";
    public static final String CODE_GUEST = "GUEST";

    // getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }
    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
} 