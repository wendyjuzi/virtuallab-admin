package com.edu.virtuallab.resource.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统概览DTO
 */
public class SystemOverview {
    private Long totalUsers;
    private Long totalResources;
    private Long totalLikes;
    private Long totalFavorites;
    private Long totalShares;
    private Double avgLikesPerUser;
    private Double avgFavoritesPerUser;
    private Double avgLikesPerResource;
    private Double avgFavoritesPerResource;
    private Map<String, Long> dailyStats;
    private List<AdminStatsResponse.UserLikeFavoriteStats> topUsers;
    private List<ResourceStatsResponse.ResourceLikeFavoriteStats> topResources;
    private Date lastUpdateTime;

    public SystemOverview() {}

    public SystemOverview(Long totalUsers, Long totalResources, Long totalLikes, Long totalFavorites, Long totalShares) {
        this.totalUsers = totalUsers;
        this.totalResources = totalResources;
        this.totalLikes = totalLikes;
        this.totalFavorites = totalFavorites;
        this.totalShares = totalShares;
    }

    // Getters and Setters
    public Long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }
    public Long getTotalResources() { return totalResources; }
    public void setTotalResources(Long totalResources) { this.totalResources = totalResources; }
    public Long getTotalLikes() { return totalLikes; }
    public void setTotalLikes(Long totalLikes) { this.totalLikes = totalLikes; }
    public Long getTotalFavorites() { return totalFavorites; }
    public void setTotalFavorites(Long totalFavorites) { this.totalFavorites = totalFavorites; }
    public Long getTotalShares() { return totalShares; }
    public void setTotalShares(Long totalShares) { this.totalShares = totalShares; }
    public Double getAvgLikesPerUser() { return avgLikesPerUser; }
    public void setAvgLikesPerUser(Double avgLikesPerUser) { this.avgLikesPerUser = avgLikesPerUser; }
    public Double getAvgFavoritesPerUser() { return avgFavoritesPerUser; }
    public void setAvgFavoritesPerUser(Double avgFavoritesPerUser) { this.avgFavoritesPerUser = avgFavoritesPerUser; }
    public Double getAvgLikesPerResource() { return avgLikesPerResource; }
    public void setAvgLikesPerResource(Double avgLikesPerResource) { this.avgLikesPerResource = avgLikesPerResource; }
    public Double getAvgFavoritesPerResource() { return avgFavoritesPerResource; }
    public void setAvgFavoritesPerResource(Double avgFavoritesPerResource) { this.avgFavoritesPerResource = avgFavoritesPerResource; }
    public Map<String, Long> getDailyStats() { return dailyStats; }
    public void setDailyStats(Map<String, Long> dailyStats) { this.dailyStats = dailyStats; }
    public List<AdminStatsResponse.UserLikeFavoriteStats> getTopUsers() { return topUsers; }
    public void setTopUsers(List<AdminStatsResponse.UserLikeFavoriteStats> topUsers) { this.topUsers = topUsers; }
    public List<ResourceStatsResponse.ResourceLikeFavoriteStats> getTopResources() { return topResources; }
    public void setTopResources(List<ResourceStatsResponse.ResourceLikeFavoriteStats> topResources) { this.topResources = topResources; }
    public Date getLastUpdateTime() { return lastUpdateTime; }
    public void setLastUpdateTime(Date lastUpdateTime) { this.lastUpdateTime = lastUpdateTime; }
} 