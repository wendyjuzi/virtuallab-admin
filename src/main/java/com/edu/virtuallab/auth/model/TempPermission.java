package com.edu.virtuallab.auth.model;

import java.util.Date;

public class TempPermission {
    private Long id;
    private Long userId;
    private Long roleId;
    private Long permissionId;
    private String reason; // 权限分配原因
    private Date startTime; // 权限生效时间
    private Date endTime; // 权限失效时间
    private Integer status; // 0-待生效 1-生效中 2-已失效
    private Long grantBy; // 授权人ID
    private String grantReason; // 授权原因
    private Date createTime;
    private Date updateTime;

    // 状态常量
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_EXPIRED = 2;

    // getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
    public Long getPermissionId() { return permissionId; }
    public void setPermissionId(Long permissionId) { this.permissionId = permissionId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }
    public Date getEndTime() { return endTime; }
    public void setEndTime(Date endTime) { this.endTime = endTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getGrantBy() { return grantBy; }
    public void setGrantBy(Long grantBy) { this.grantBy = grantBy; }
    public String getGrantReason() { return grantReason; }
    public void setGrantReason(String grantReason) { this.grantReason = grantReason; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
} 