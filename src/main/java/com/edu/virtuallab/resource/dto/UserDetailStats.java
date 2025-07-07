package com.edu.virtuallab.resource.dto;

import java.util.Date;
import java.util.List;

/**
 * 用户详细统计DTO
 */
public class UserDetailStats {
    private Long userId;
    private String username;
    private String nickname;
    private String email;
    private Long likeCount;
    private Long favoriteCount;
    private Long totalInteractions;
    private Date registerTime;
    private List<LikeRecordWithUser> recentLikes;
    private List<FavoriteRecordWithUser> recentFavorites;

    public UserDetailStats() {}

    public UserDetailStats(Long userId, String username, String nickname, 
                          Long likeCount, Long favoriteCount) {
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
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Long getLikeCount() { return likeCount; }
    public void setLikeCount(Long likeCount) { this.likeCount = likeCount; }
    public Long getFavoriteCount() { return favoriteCount; }
    public void setFavoriteCount(Long favoriteCount) { this.favoriteCount = favoriteCount; }
    public Long getTotalInteractions() { return totalInteractions; }
    public void setTotalInteractions(Long totalInteractions) { this.totalInteractions = totalInteractions; }
    public Date getRegisterTime() { return registerTime; }
    public void setRegisterTime(Date registerTime) { this.registerTime = registerTime; }
    public List<LikeRecordWithUser> getRecentLikes() { return recentLikes; }
    public void setRecentLikes(List<LikeRecordWithUser> recentLikes) { this.recentLikes = recentLikes; }
    public List<FavoriteRecordWithUser> getRecentFavorites() { return recentFavorites; }
    public void setRecentFavorites(List<FavoriteRecordWithUser> recentFavorites) { this.recentFavorites = recentFavorites; }
} 