package com.edu.virtuallab.auth.dto;

import java.util.List;

public class PermissionTemplateDTO {
    private Long id;
    private String name; // 模板名称
    private String code; // 模板编码
    private String description; // 模板描述
    private String roleType; // 适用角色类型
    private List<Long> permissionIds; // 权限ID列表
    private Integer status; // 0-禁用 1-启用
    private String createdBy; // 创建人

    // getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRoleType() { return roleType; }
    public void setRoleType(String roleType) { this.roleType = roleType; }
    public List<Long> getPermissionIds() { return permissionIds; }
    public void setPermissionIds(List<Long> permissionIds) { this.permissionIds = permissionIds; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
} 