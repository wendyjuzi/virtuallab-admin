package com.edu.virtuallab.resource.dto;

import java.util.Date;
import java.util.List;

/**
 * 资源详细统计DTO
 */
public class ResourceDetailStats {
    private Long resourceId;
    private String resourceName;
    private String resourceType;
    private String description;
    private Long likeCount;
    private Long favoriteCount;
    private Long totalInteractions;
    private Date createTime;
    private List<LikeRecordWithUser> recentLikes;
    private List<FavoriteRecordWithUser> recentFavorites;

    public ResourceDetailStats() {}

    public ResourceDetailStats(Long resourceId, String resourceName, String resourceType, 
                              Long likeCount, Long favoriteCount) {
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.likeCount = likeCount;
        this.favoriteCount = favoriteCount;
        this.totalInteractions = likeCount + favoriteCount;
    }

    // Getters and Setters
    public Long getResourceId() { return resourceId; }
    public void setResourceId(Long resourceId) { this.resourceId = resourceId; }
    public String getResourceName() { return resourceName; }
    public void setResourceName(String resourceName) { this.resourceName = resourceName; }
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getLikeCount() { return likeCount; }
    public void setLikeCount(Long likeCount) { this.likeCount = likeCount; }
    public Long getFavoriteCount() { return favoriteCount; }
    public void setFavoriteCount(Long favoriteCount) { this.favoriteCount = favoriteCount; }
    public Long getTotalInteractions() { return totalInteractions; }
    public void setTotalInteractions(Long totalInteractions) { this.totalInteractions = totalInteractions; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public List<LikeRecordWithUser> getRecentLikes() { return recentLikes; }
    public void setRecentLikes(List<LikeRecordWithUser> recentLikes) { this.recentLikes = recentLikes; }
    public List<FavoriteRecordWithUser> getRecentFavorites() { return recentFavorites; }
    public void setRecentFavorites(List<FavoriteRecordWithUser> recentFavorites) { this.recentFavorites = recentFavorites; }
} 