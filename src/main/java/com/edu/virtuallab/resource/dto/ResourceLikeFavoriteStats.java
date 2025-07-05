package com.edu.virtuallab.resource.dto;

import java.util.Date;

/**
 * 资源点赞收藏统计DTO
 */
public class ResourceLikeFavoriteStats {
    private Long resourceId;
    private String resourceName;
    private String resourceType;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer totalInteractions;
    private String category;
    private Date createTime;
    private Date lastInteraction;

    public ResourceLikeFavoriteStats() {}

    public ResourceLikeFavoriteStats(Long resourceId, String resourceName, String resourceType, 
                                   Integer likeCount, Integer favoriteCount, String category) {
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.likeCount = likeCount != null ? likeCount : 0;
        this.favoriteCount = favoriteCount != null ? favoriteCount : 0;
        this.totalInteractions = this.likeCount + this.favoriteCount;
        this.category = category;
    }

    // Getters and Setters
    public Long getResourceId() { return resourceId; }
    public void setResourceId(Long resourceId) { this.resourceId = resourceId; }
    
    public String getResourceName() { return resourceName; }
    public void setResourceName(String resourceName) { this.resourceName = resourceName; }
    
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    
    public Integer getLikeCount() { return likeCount != null ? likeCount : 0; }
    public void setLikeCount(Integer likeCount) { 
        this.likeCount = likeCount; 
        updateTotalInteractions();
    }
    
    public Integer getFavoriteCount() { return favoriteCount != null ? favoriteCount : 0; }
    public void setFavoriteCount(Integer favoriteCount) { 
        this.favoriteCount = favoriteCount; 
        updateTotalInteractions();
    }
    
    public Integer getTotalInteractions() { return totalInteractions; }
    public void setTotalInteractions(Integer totalInteractions) { this.totalInteractions = totalInteractions; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    
    public Date getLastInteraction() { return lastInteraction; }
    public void setLastInteraction(Date lastInteraction) { this.lastInteraction = lastInteraction; }
    
    private void updateTotalInteractions() {
        this.totalInteractions = (this.likeCount != null ? this.likeCount : 0) + 
                                (this.favoriteCount != null ? this.favoriteCount : 0);
    }
} 