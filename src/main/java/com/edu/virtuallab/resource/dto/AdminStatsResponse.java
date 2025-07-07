package com.edu.virtuallab.resource.dto;

import java.util.List;

/**
 * 管理员统计响应DTO
 */
public class AdminStatsResponse {
    private Long totalUsers;
    private Long totalLikes;
    private Long totalFavorites;
    private Long totalResources;
    private Double avgLikesPerUser;
    private Double avgFavoritesPerUser;
    private List<UserLikeFavoriteStats> topUsers;
    private List<ResourceLikeFavoriteStats> topResources;

    public AdminStatsResponse() {}

    public AdminStatsResponse(Long totalUsers, Long totalLikes, Long totalFavorites, Long totalResources) {
        this.totalUsers = totalUsers;
        this.totalLikes = totalLikes;
        this.totalFavorites = totalFavorites;
        this.totalResources = totalResources;
    }

    // Getters and Setters
    public Long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }
    public Long getTotalLikes() { return totalLikes; }
    public void setTotalLikes(Long totalLikes) { this.totalLikes = totalLikes; }
    public Long getTotalFavorites() { return totalFavorites; }
    public void setTotalFavorites(Long totalFavorites) { this.totalFavorites = totalFavorites; }
    public Long getTotalResources() { return totalResources; }
    public void setTotalResources(Long totalResources) { this.totalResources = totalResources; }
    public Double getAvgLikesPerUser() { return avgLikesPerUser; }
    public void setAvgLikesPerUser(Double avgLikesPerUser) { this.avgLikesPerUser = avgLikesPerUser; }
    public Double getAvgFavoritesPerUser() { return avgFavoritesPerUser; }
    public void setAvgFavoritesPerUser(Double avgFavoritesPerUser) { this.avgFavoritesPerUser = avgFavoritesPerUser; }
    public List<UserLikeFavoriteStats> getTopUsers() { return topUsers; }
    public void setTopUsers(List<UserLikeFavoriteStats> topUsers) { this.topUsers = topUsers; }
    public List<ResourceLikeFavoriteStats> getTopResources() { return topResources; }
    public void setTopResources(List<ResourceLikeFavoriteStats> topResources) { this.topResources = topResources; }

    /**
     * 用户点赞收藏统计
     */
    public static class UserLikeFavoriteStats {
        private Long userId;
        private String username;
        private String nickname;
        private Long likeCount;
        private Long favoriteCount;
        private Long totalInteractions;
        private java.util.Date lastActivity;
        private java.util.Date joinTime;

        public UserLikeFavoriteStats() {}

        public UserLikeFavoriteStats(Long userId, String username, String nickname, Long likeCount, Long favoriteCount) {
            this.userId = userId;
            this.username = username;
            this.nickname = nickname;
            this.likeCount = likeCount;
            this.favoriteCount = favoriteCount;
            this.totalInteractions = likeCount + favoriteCount;
        }

        // Getters and Setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getNickname() { return nickname; }
        public void setNickname(String nickname) { this.nickname = nickname; }
        public Long getLikeCount() { return likeCount; }
        public void setLikeCount(Long likeCount) { this.likeCount = likeCount; }
        public Long getFavoriteCount() { return favoriteCount; }
        public void setFavoriteCount(Long favoriteCount) { this.favoriteCount = favoriteCount; }
        public Long getTotalInteractions() { return totalInteractions; }
        public void setTotalInteractions(Long totalInteractions) { this.totalInteractions = totalInteractions; }
        public java.util.Date getLastActivity() { return lastActivity; }
        public void setLastActivity(java.util.Date lastActivity) { this.lastActivity = lastActivity; }
        public java.util.Date getJoinTime() { return joinTime; }
        public void setJoinTime(java.util.Date joinTime) { this.joinTime = joinTime; }
    }

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