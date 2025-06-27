package com.edu.virtuallab.auth.model;

import java.util.Date;

public class Permission {
    private Long id;
    private String name;
    private String code;
    private String type; // 权限类型：menu-菜单 permission-权限
    private String module; // 功能模块：experiment-实验管理 resource-资源库 score-成绩管理 user-用户管理 system-系统管理
    private String action; // 操作类型：create-创建 view-查看 edit-编辑 delete-删除 approve-审批
    private String resource; // 资源标识
    private String description;
    private Integer status; // 0-禁用 1-启用
    private Long parentId; // 父权限ID，用于权限树结构
    private Integer sort; // 排序
    private String icon; // 图标
    private String path; // 路径
    private Date createTime;
    private Date updateTime;

    // 权限类型常量
    public static final String TYPE_MENU = "menu";
    public static final String TYPE_PERMISSION = "permission";

    // 功能模块常量
    public static final String MODULE_EXPERIMENT = "experiment";
    public static final String MODULE_RESOURCE = "resource";
    public static final String MODULE_SCORE = "score";
    public static final String MODULE_USER = "user";
    public static final String MODULE_SYSTEM = "system";

    // 操作类型常量
    public static final String ACTION_CREATE = "create";
    public static final String ACTION_VIEW = "view";
    public static final String ACTION_EDIT = "edit";
    public static final String ACTION_DELETE = "delete";
    public static final String ACTION_APPROVE = "approve";

    // getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getResource() { return resource; }
    public void setResource(String resource) { this.resource = resource; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public Integer getSort() { return sort; }
    public void setSort(Integer sort) { this.sort = sort; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
} 