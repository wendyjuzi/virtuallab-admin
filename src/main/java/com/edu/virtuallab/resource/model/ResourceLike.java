package com.edu.virtuallab.resource.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 资源点赞实体类
 */
public class ResourceLike implements Serializable {
    private Long id;
    private Long userId;
    private Long resourceId;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
} 