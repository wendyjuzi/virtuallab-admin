package com.edu.virtuallab.resource.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 资源收藏实体类
 */
public class ResourceFavorite implements Serializable {
    private Long id;
    private Long userId;
    private Long resourceId;
    private Date createTime;

    public ResourceFavorite() {}

    public ResourceFavorite(Long userId, Long resourceId) {
        this.userId = userId;
        this.resourceId = resourceId;
        this.createTime = new Date();
    }

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

    @Override
    public String toString() {
        return "ResourceFavorite{" +
                "id=" + id +
                ", userId=" + userId +
                ", resourceId=" + resourceId +
                ", createTime=" + createTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceFavorite that = (ResourceFavorite) o;
        return userId.equals(that.userId) && resourceId.equals(that.resourceId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode() * 31 + resourceId.hashCode();
    }
}
