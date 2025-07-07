package com.edu.virtuallab.common.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存管理器
 * 提供统一的缓存操作接口，支持实时更新和失效机制
 */
@Component
public class CacheManager {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 缓存前缀常量
    public static final String USER_CACHE_PREFIX = "user:";
    public static final String RESOURCE_CACHE_PREFIX = "resource:";
    public static final String PERMISSION_CACHE_PREFIX = "permission:";
    public static final String SESSION_CACHE_PREFIX = "session:";
    public static final String STATISTICS_CACHE_PREFIX = "statistics:";

    /**
     * 设置缓存
     */
    public void set(String key, Object value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    /**
     * 设置缓存（无过期时间）
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取缓存
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除缓存
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 批量删除缓存
     */
    public void deleteByPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 检查缓存是否存在
     */
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 设置过期时间
     */
    public void expire(String key, Duration duration) {
        redisTemplate.expire(key, duration);
    }

    /**
     * 获取剩余过期时间
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 用户相关缓存操作
     */
    public void setUserCache(String userId, Object value, Duration duration) {
        set(USER_CACHE_PREFIX + userId, value, duration);
    }

    public Object getUserCache(String userId) {
        return get(USER_CACHE_PREFIX + userId);
    }

    public void deleteUserCache(String userId) {
        delete(USER_CACHE_PREFIX + userId);
    }

    public void deleteAllUserCache() {
        deleteByPattern(USER_CACHE_PREFIX + "*");
    }

    /**
     * 资源相关缓存操作
     */
    public void setResourceCache(String resourceId, Object value, Duration duration) {
        set(RESOURCE_CACHE_PREFIX + resourceId, value, duration);
    }

    public Object getResourceCache(String resourceId) {
        return get(RESOURCE_CACHE_PREFIX + resourceId);
    }

    public void deleteResourceCache(String resourceId) {
        delete(RESOURCE_CACHE_PREFIX + resourceId);
    }

    public void deleteAllResourceCache() {
        deleteByPattern(RESOURCE_CACHE_PREFIX + "*");
    }

    /**
     * 权限相关缓存操作
     */
    public void setPermissionCache(String userId, Object value, Duration duration) {
        set(PERMISSION_CACHE_PREFIX + userId, value, duration);
    }

    public Object getPermissionCache(String userId) {
        return get(PERMISSION_CACHE_PREFIX + userId);
    }

    public void deletePermissionCache(String userId) {
        delete(PERMISSION_CACHE_PREFIX + userId);
    }

    public void deleteAllPermissionCache() {
        deleteByPattern(PERMISSION_CACHE_PREFIX + "*");
    }

    /**
     * 会话相关缓存操作
     */
    public void setSessionCache(String sessionId, Object value, Duration duration) {
        set(SESSION_CACHE_PREFIX + sessionId, value, duration);
    }

    public Object getSessionCache(String sessionId) {
        return get(SESSION_CACHE_PREFIX + sessionId);
    }

    public void deleteSessionCache(String sessionId) {
        delete(SESSION_CACHE_PREFIX + sessionId);
    }

    /**
     * 统计相关缓存操作
     */
    public void setStatisticsCache(String key, Object value, Duration duration) {
        set(STATISTICS_CACHE_PREFIX + key, value, duration);
    }

    public Object getStatisticsCache(String key) {
        return get(STATISTICS_CACHE_PREFIX + key);
    }

    public void deleteStatisticsCache(String key) {
        delete(STATISTICS_CACHE_PREFIX + key);
    }

    public void deleteAllStatisticsCache() {
        deleteByPattern(STATISTICS_CACHE_PREFIX + "*");
    }

    /**
     * 清除所有缓存
     */
    public void clearAllCache() {
        deleteByPattern("*");
    }

    /**
     * 获取缓存统计信息
     */
    public CacheStatistics getCacheStatistics() {
        CacheStatistics statistics = new CacheStatistics();
        
        // 统计各种类型的缓存数量
        Set<String> userKeys = redisTemplate.keys(USER_CACHE_PREFIX + "*");
        Set<String> resourceKeys = redisTemplate.keys(RESOURCE_CACHE_PREFIX + "*");
        Set<String> permissionKeys = redisTemplate.keys(PERMISSION_CACHE_PREFIX + "*");
        Set<String> sessionKeys = redisTemplate.keys(SESSION_CACHE_PREFIX + "*");
        Set<String> statisticsKeys = redisTemplate.keys(STATISTICS_CACHE_PREFIX + "*");

        statistics.setUserCacheCount(userKeys != null ? userKeys.size() : 0);
        statistics.setResourceCacheCount(resourceKeys != null ? resourceKeys.size() : 0);
        statistics.setPermissionCacheCount(permissionKeys != null ? permissionKeys.size() : 0);
        statistics.setSessionCacheCount(sessionKeys != null ? sessionKeys.size() : 0);
        statistics.setStatisticsCacheCount(statisticsKeys != null ? statisticsKeys.size() : 0);

        return statistics;
    }

    /**
     * 缓存统计信息
     */
    public static class CacheStatistics {
        private int userCacheCount;
        private int resourceCacheCount;
        private int permissionCacheCount;
        private int sessionCacheCount;
        private int statisticsCacheCount;

        // getters and setters
        public int getUserCacheCount() { return userCacheCount; }
        public void setUserCacheCount(int userCacheCount) { this.userCacheCount = userCacheCount; }
        public int getResourceCacheCount() { return resourceCacheCount; }
        public void setResourceCacheCount(int resourceCacheCount) { this.resourceCacheCount = resourceCacheCount; }
        public int getPermissionCacheCount() { return permissionCacheCount; }
        public void setPermissionCacheCount(int permissionCacheCount) { this.permissionCacheCount = permissionCacheCount; }
        public int getSessionCacheCount() { return sessionCacheCount; }
        public void setSessionCacheCount(int sessionCacheCount) { this.sessionCacheCount = sessionCacheCount; }
        public int getStatisticsCacheCount() { return statisticsCacheCount; }
        public void setStatisticsCacheCount(int statisticsCacheCount) { this.statisticsCacheCount = statisticsCacheCount; }
    }
} 