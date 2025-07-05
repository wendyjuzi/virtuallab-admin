package com.edu.virtuallab.resource.dto;

import java.util.List;

/**
 * 资源统计响应DTO
 */
public class ResourceStatsResponse {
    private Long totalResources;
    private Long totalLikes;
    private Long totalFavorites;
    private Double avgLikesPerResource;
    private Double avgFavoritesPerResource;
    private List<ResourceLikeFavoriteStats> topResources;

    public ResourceStatsResponse() {}

    public ResourceStatsResponse(Long totalResources, Long totalLikes, Long totalFavorites) {
        this.totalResources = totalResources;
        this.totalLikes = totalLikes;
        this.totalFavorites = totalFavorites;
    }

    // Getters and Setters
    public Long getTotalResources() { return totalResources; }
    public void setTotalResources(Long totalResources) { this.totalResources = totalResources; }
    public Long getTotalLikes() { return totalLikes; }
    public void setTotalLikes(Long totalLikes) { this.totalLikes = totalLikes; }
    public Long getTotalFavorites() { return totalFavorites; }
    public void setTotalFavorites(Long totalFavorites) { this.totalFavorites = totalFavorites; }
    public Double getAvgLikesPerResource() { return avgLikesPerResource; }
    public void setAvgLikesPerResource(Double avgLikesPerResource) { this.avgLikesPerResource = avgLikesPerResource; }
    public Double getAvgFavoritesPerResource() { return avgFavoritesPerResource; }
    public void setAvgFavoritesPerResource(Double avgFavoritesPerResource) { this.avgFavoritesPerResource = avgFavoritesPerResource; }
    public List<ResourceLikeFavoriteStats> getTopResources() { return topResources; }
    public void setTopResources(List<ResourceLikeFavoriteStats> topResources) { this.topResources = topResources; }

    /**
     * 资源点赞收藏统计
     */
    public static class ResourceLikeFavoriteStats {
        private Long resourceId;
        private String resourceName;
        private String resourceType;
        private Long likeCount;
        private Long favoriteCount;
        private Long totalInteractions;
        private String category;
        private java.util.Date createTime;
        private java.util.Date lastInteraction;

        public ResourceLikeFavoriteStats() {}

        public ResourceLikeFavoriteStats(Long resourceId, String resourceName, String resourceType, Long likeCount, Long favoriteCount) {
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
        public Long getLikeCount() { return likeCount; }
        public void setLikeCount(Long likeCount) { this.likeCount = likeCount; }
        public Long getFavoriteCount() { return favoriteCount; }
        public void setFavoriteCount(Long favoriteCount) { this.favoriteCount = favoriteCount; }
        public Long getTotalInteractions() { return totalInteractions; }
        public void setTotalInteractions(Long totalInteractions) { this.totalInteractions = totalInteractions; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public java.util.Date getCreateTime() { return createTime; }
        public void setCreateTime(java.util.Date createTime) { this.createTime = createTime; }
        public java.util.Date getLastInteraction() { return lastInteraction; }
        public void setLastInteraction(java.util.Date lastInteraction) { this.lastInteraction = lastInteraction; }
    }
} 