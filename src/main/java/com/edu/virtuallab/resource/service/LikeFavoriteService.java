package com.edu.virtuallab.resource.service;

import com.edu.virtuallab.resource.model.LikeFavoriteStatus;

/**
 * 查询某个实验的点赞/收藏状态
 */
public interface LikeFavoriteService {
    LikeFavoriteStatus getStatus(Long resourceId, Long userId);
    void like(Long resourceId, Long userId);
    void unlike(Long resourceId, Long userId);
    void favorite(Long resourceId, Long userId);
    void unfavorite(Long resourceId, Long userId);
    java.util.List<com.edu.virtuallab.resource.model.ResourceLike> getUserLikes(Long userId, int page, int size);
    int countUserLikes(Long userId);
    boolean isResourceLiked(Long userId, Long resourceId);
    int countResourceLikes(Long resourceId);
} 