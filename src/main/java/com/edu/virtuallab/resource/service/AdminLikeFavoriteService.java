package com.edu.virtuallab.resource.service;

import com.edu.virtuallab.resource.dto.*;

import java.util.List;

/**
 * 管理员点赞收藏服务接口
 */
public interface AdminLikeFavoriteService {
    
    /**
     * 获取所有用户点赞收藏统计
     */
    AdminStatsResponse getAllUsersLikeFavoriteStats();
    
    /**
     * 获取所有点赞记录（分页）
     */
    PageResponse<LikeRecordWithUser> getAllLikes(int page, int size);
    
    /**
     * 获取所有收藏记录（分页）
     */
    PageResponse<FavoriteRecordWithUser> getAllFavorites(int page, int size);
    
    /**
     * 删除指定点赞记录
     */
    Integer adminDeleteLike(Long likeId);
    
    /**
     * 批量删除点赞记录
     */
    Integer adminBatchDeleteLikes(List<Long> likeIds);
    
    /**
     * 删除指定收藏记录
     */
    Integer adminDeleteFavorite(Long favoriteId);
    
    /**
     * 批量删除收藏记录
     */
    Integer adminBatchDeleteFavorites(List<Long> favoriteIds);
    
    /**
     * 重置用户统计
     */
    void adminResetUserStats(Long userId);
    
    /**
     * 获取资源统计
     */
    ResourceStatsResponse getResourceLikeFavoriteStats();
    
    /**
     * 获取指定资源统计
     */
    ResourceDetailStats getResourceDetailStats(Long resourceId);
    
    /**
     * 获取指定用户统计
     */
    UserDetailStats getUserDetailStats(Long userId);
    
    /**
     * 导出数据
     */
    byte[] exportLikeFavoriteData(String format);
    
    /**
     * 系统概览
     */
    SystemOverview getSystemOverview();
} 