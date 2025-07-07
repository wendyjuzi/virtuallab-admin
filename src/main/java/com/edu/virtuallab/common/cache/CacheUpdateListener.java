package com.edu.virtuallab.common.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 缓存更新监听器
 * 监听数据变更事件，自动更新相关缓存
 */
@Component
public class CacheUpdateListener {

    @Autowired
    private CacheManager cacheManager;

    /**
     * 用户数据变更时更新缓存
     */
    public void onUserDataChanged(String userId) {
        // 清除用户相关缓存
        cacheManager.deleteUserCache(userId);
        cacheManager.deletePermissionCache(userId);
        
        // 清除统计缓存
        cacheManager.deleteStatisticsCache("user_count");
        cacheManager.deleteStatisticsCache("online_users");
    }

    /**
     * 资源数据变更时更新缓存
     */
    public void onResourceDataChanged(String resourceId) {
        // 清除资源相关缓存
        cacheManager.deleteResourceCache(resourceId);
        
        // 清除统计缓存
        cacheManager.deleteStatisticsCache("resource_count");
        cacheManager.deleteStatisticsCache("resource_stats");
    }

    /**
     * 权限数据变更时更新缓存
     */
    public void onPermissionDataChanged(String userId) {
        // 清除权限相关缓存
        cacheManager.deletePermissionCache(userId);
        
        // 清除统计缓存
        cacheManager.deleteStatisticsCache("permission_stats");
    }

    /**
     * 点赞/收藏数据变更时更新缓存
     */
    public void onLikeFavoriteDataChanged(String resourceId) {
        // 清除资源相关缓存
        cacheManager.deleteResourceCache(resourceId);
        
        // 清除统计缓存
        cacheManager.deleteStatisticsCache("like_count_" + resourceId);
        cacheManager.deleteStatisticsCache("favorite_count_" + resourceId);
    }

    /**
     * 分享数据变更时更新缓存
     */
    public void onShareDataChanged(String resourceId) {
        // 清除资源相关缓存
        cacheManager.deleteResourceCache(resourceId);
        
        // 清除统计缓存
        cacheManager.deleteStatisticsCache("share_count_" + resourceId);
    }

    /**
     * 评论数据变更时更新缓存
     */
    public void onCommentDataChanged(String resourceId) {
        // 清除资源相关缓存
        cacheManager.deleteResourceCache(resourceId);
        
        // 清除统计缓存
        cacheManager.deleteStatisticsCache("comment_count_" + resourceId);
    }

    /**
     * 实验数据变更时更新缓存
     */
    public void onExperimentDataChanged(String experimentId) {
        // 清除实验相关缓存
        cacheManager.delete("experiment:" + experimentId);
        
        // 清除统计缓存
        cacheManager.deleteStatisticsCache("experiment_count");
        cacheManager.deleteStatisticsCache("active_experiments");
    }

    /**
     * 系统统计数据变更时更新缓存
     */
    public void onSystemStatsChanged() {
        // 清除所有统计缓存
        cacheManager.deleteAllStatisticsCache();
    }

    /**
     * 批量更新缓存
     */
    public void batchUpdateCache(String[] userIds, String[] resourceIds) {
        if (userIds != null) {
            for (String userId : userIds) {
                onUserDataChanged(userId);
            }
        }
        
        if (resourceIds != null) {
            for (String resourceId : resourceIds) {
                onResourceDataChanged(resourceId);
            }
        }
    }

    /**
     * 设置缓存并监听变更
     */
    public void setCacheWithListener(String key, Object value, Duration duration, String type) {
        cacheManager.set(key, value, duration);
        
        // 根据类型设置监听
        switch (type) {
            case "user":
                // 用户缓存监听逻辑
                break;
            case "resource":
                // 资源缓存监听逻辑
                break;
            case "permission":
                // 权限缓存监听逻辑
                break;
            default:
                break;
        }
    }

    /**
     * 强制刷新所有缓存
     */
    public void refreshAllCache() {
        cacheManager.clearAllCache();
    }

    /**
     * 获取缓存更新统计
     */
    public CacheUpdateStatistics getUpdateStatistics() {
        CacheUpdateStatistics stats = new CacheUpdateStatistics();
        
        // 这里可以添加缓存更新统计逻辑
        stats.setTotalUpdates(0);
        stats.setUserCacheUpdates(0);
        stats.setResourceCacheUpdates(0);
        stats.setPermissionCacheUpdates(0);
        
        return stats;
    }

    /**
     * 缓存更新统计信息
     */
    public static class CacheUpdateStatistics {
        private int totalUpdates;
        private int userCacheUpdates;
        private int resourceCacheUpdates;
        private int permissionCacheUpdates;

        public int getTotalUpdates() { return totalUpdates; }
        public void setTotalUpdates(int totalUpdates) { this.totalUpdates = totalUpdates; }
        public int getUserCacheUpdates() { return userCacheUpdates; }
        public void setUserCacheUpdates(int userCacheUpdates) { this.userCacheUpdates = userCacheUpdates; }
        public int getResourceCacheUpdates() { return resourceCacheUpdates; }
        public void setResourceCacheUpdates(int resourceCacheUpdates) { this.resourceCacheUpdates = resourceCacheUpdates; }
        public int getPermissionCacheUpdates() { return permissionCacheUpdates; }
        public void setPermissionCacheUpdates(int permissionCacheUpdates) { this.permissionCacheUpdates = permissionCacheUpdates; }
    }
} 