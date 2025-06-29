package com.edu.virtuallab.auth.model;

import java.util.Date;

public class Department {
    private Long id;
    private String name; // 院系名称
    private String code; // 院系编码
    private String description; // 院系描述
    private Long parentId; // 父院系ID，用于院系层级结构
    private Integer level; // 院系级别：1-学院 2-系 3-专业
    private Integer status; // 0-禁用 1-启用
    private String contactPerson; // 联系人
    private String contactPhone; // 联系电话
    private String contactEmail; // 联系邮箱
    private Integer sort; // 排序
    private Date createTime;
    private Date updateTime;

    // 院系级别常量
    public static final int LEVEL_COLLEGE = 1;
    public static final int LEVEL_DEPARTMENT = 2;
    public static final int LEVEL_MAJOR = 3;

    // getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public Integer getSort() { return sort; }
    public void setSort(Integer sort) { this.sort = sort; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
} 