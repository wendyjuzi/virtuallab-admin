package com.edu.virtuallab.resource.dto;

import java.util.Date;

/**
 * 用户点赞收藏统计DTO
 */
public class UserLikeFavoriteStats {
    private Long userId;
    private String username;
    private String nickname;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer totalInteractions;
    private Date lastActivity;
    private Date joinTime;

    public UserLikeFavoriteStats() {}

    public UserLikeFavoriteStats(Long userId, String username, String nickname, 
                                Integer likeCount, Integer favoriteCount, Date lastActivity) {
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
        this.likeCount = likeCount != null ? likeCount : 0;
        this.favoriteCount = favoriteCount != null ? favoriteCount : 0;
        this.totalInteractions = this.likeCount + this.favoriteCount;
        this.lastActivity = lastActivity;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    
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
    
    public Date getLastActivity() { return lastActivity; }
    public void setLastActivity(Date lastActivity) { this.lastActivity = lastActivity; }
    
    public Date getJoinTime() { return joinTime; }
    public void setJoinTime(Date joinTime) { this.joinTime = joinTime; }
    
    private void updateTotalInteractions() {
        this.totalInteractions = (this.likeCount != null ? this.likeCount : 0) + 
                                (this.favoriteCount != null ? this.favoriteCount : 0);
    }
} 