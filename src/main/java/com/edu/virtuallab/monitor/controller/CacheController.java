package com.edu.virtuallab.monitor.controller;

import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.common.cache.CacheManager;
import com.edu.virtuallab.common.cache.CacheUpdateListener;
import com.edu.virtuallab.monitor.util.UserSessionCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

/**
 * 缓存管理控制器
 * 提供缓存查询、更新、清除等管理功能
 */
@RestController
@RequestMapping("/cache")
public class CacheController {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private CacheUpdateListener cacheUpdateListener;

    @Autowired
    private UserSessionCache userSessionCache;

    /**
     * 获取缓存统计信息
     */
    @GetMapping("/statistics")
    public CommonResult<CacheManager.CacheStatistics> getCacheStatistics() {
        try {
            CacheManager.CacheStatistics statistics = cacheManager.getCacheStatistics();
            return CommonResult.success(statistics, "获取缓存统计成功");
        } catch (Exception e) {
            return CommonResult.failed("获取缓存统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户会话统计信息
     */
    @GetMapping("/session-statistics")
    public CommonResult<UserSessionCache.SessionStatistics> getSessionStatistics() {
        try {
            UserSessionCache.SessionStatistics statistics = userSessionCache.getSessionStatistics();
            return CommonResult.success(statistics, "获取会话统计成功");
        } catch (Exception e) {
            return CommonResult.failed("获取会话统计失败: " + e.getMessage());
        }
    }

    /**
     * 清除指定用户的缓存
     */
    @DeleteMapping("/user/{userId}")
    public CommonResult<String> clearUserCache(@PathVariable String userId) {
        try {
            cacheUpdateListener.onUserDataChanged(userId);
            return CommonResult.success("清除用户缓存成功", "清除用户缓存成功");
        } catch (Exception e) {
            return CommonResult.failed("清除用户缓存失败: " + e.getMessage());
        }
    }

    /**
     * 清除指定资源的缓存
     */
    @DeleteMapping("/resource/{resourceId}")
    public CommonResult<String> clearResourceCache(@PathVariable String resourceId) {
        try {
            cacheUpdateListener.onResourceDataChanged(resourceId);
            return CommonResult.success("清除资源缓存成功", "清除资源缓存成功");
        } catch (Exception e) {
            return CommonResult.failed("清除资源缓存失败: " + e.getMessage());
        }
    }

    /**
     * 清除指定用户的权限缓存
     */
    @DeleteMapping("/permission/{userId}")
    public CommonResult<String> clearPermissionCache(@PathVariable String userId) {
        try {
            cacheUpdateListener.onPermissionDataChanged(userId);
            return CommonResult.success("清除权限缓存成功", "清除权限缓存成功");
        } catch (Exception e) {
            return CommonResult.failed("清除权限缓存失败: " + e.getMessage());
        }
    }

    /**
     * 清除点赞/收藏缓存
     */
    @DeleteMapping("/like-favorite/{resourceId}")
    public CommonResult<String> clearLikeFavoriteCache(@PathVariable String resourceId) {
        try {
            cacheUpdateListener.onLikeFavoriteDataChanged(resourceId);
            return CommonResult.success("清除点赞收藏缓存成功", "清除点赞收藏缓存成功");
        } catch (Exception e) {
            return CommonResult.failed("清除点赞收藏缓存失败: " + e.getMessage());
        }
    }

    /**
     * 清除分享缓存
     */
    @DeleteMapping("/share/{resourceId}")
    public CommonResult<String> clearShareCache(@PathVariable String resourceId) {
        try {
            cacheUpdateListener.onShareDataChanged(resourceId);
            return CommonResult.success("清除分享缓存成功", "清除分享缓存成功");
        } catch (Exception e) {
            return CommonResult.failed("清除分享缓存失败: " + e.getMessage());
        }
    }

    /**
     * 清除评论缓存
     */
    @DeleteMapping("/comment/{resourceId}")
    public CommonResult<String> clearCommentCache(@PathVariable String resourceId) {
        try {
            cacheUpdateListener.onCommentDataChanged(resourceId);
            return CommonResult.success("清除评论缓存成功", "清除评论缓存成功");
        } catch (Exception e) {
            return CommonResult.failed("清除评论缓存失败: " + e.getMessage());
        }
    }

    /**
     * 清除实验缓存
     */
    @DeleteMapping("/experiment/{experimentId}")
    public CommonResult<String> clearExperimentCache(@PathVariable String experimentId) {
        try {
            cacheUpdateListener.onExperimentDataChanged(experimentId);
            return CommonResult.success("清除实验缓存成功", "清除实验缓存成功");
        } catch (Exception e) {
            return CommonResult.failed("清除实验缓存失败: " + e.getMessage());
        }
    }

    /**
     * 清除统计缓存
     */
    @DeleteMapping("/statistics")
    public CommonResult<String> clearStatisticsCache() {
        try {
            cacheUpdateListener.onSystemStatsChanged();
            return CommonResult.success("清除统计缓存成功", "清除统计缓存成功");
        } catch (Exception e) {
            return CommonResult.failed("清除统计缓存失败: " + e.getMessage());
        }
    }

    /**
     * 清除所有缓存
     */
    @DeleteMapping("/all")
    public CommonResult<String> clearAllCache() {
        try {
            cacheUpdateListener.refreshAllCache();
            return CommonResult.success("清除所有缓存成功", "清除所有缓存成功");
        } catch (Exception e) {
            return CommonResult.failed("清除所有缓存失败: " + e.getMessage());
        }
    }

    /**
     * 强制用户下线
     */
    @PostMapping("/force-logout/{userId}")
    public CommonResult<String> forceLogout(@PathVariable String userId) {
        try {
            userSessionCache.forceLogout(userId);
            return CommonResult.success("强制用户下线成功", "强制用户下线成功");
        } catch (Exception e) {
            return CommonResult.failed("强制用户下线失败: " + e.getMessage());
        }
    }

    /**
     * 清理过期会话
     */
    @PostMapping("/cleanup-sessions")
    public CommonResult<String> cleanupExpiredSessions() {
        try {
            userSessionCache.cleanupExpiredSessions();
            return CommonResult.success("清理过期会话成功", "清理过期会话成功");
        } catch (Exception e) {
            return CommonResult.failed("清理过期会话失败: " + e.getMessage());
        }
    }

    /**
     * 设置缓存
     */
    @PostMapping("/set")
    public CommonResult<String> setCache(@RequestParam String key, 
                                        @RequestParam String value, 
                                        @RequestParam(defaultValue = "3600") long seconds) {
        try {
            cacheManager.set(key, value, Duration.ofSeconds(seconds));
            return CommonResult.success("设置缓存成功", "设置缓存成功");
        } catch (Exception e) {
            return CommonResult.failed("设置缓存失败: " + e.getMessage());
        }
    }

    /**
     * 获取缓存
     */
    @GetMapping("/get/{key}")
    public CommonResult<Object> getCache(@PathVariable String key) {
        try {
            Object value = cacheManager.get(key);
            return CommonResult.success(value, "获取缓存成功");
        } catch (Exception e) {
            return CommonResult.failed("获取缓存失败: " + e.getMessage());
        }
    }

    /**
     * 删除缓存
     */
    @DeleteMapping("/key/{key}")
    public CommonResult<String> deleteCache(@PathVariable String key) {
        try {
            cacheManager.delete(key);
            return CommonResult.success("删除缓存成功", "删除缓存成功");
        } catch (Exception e) {
            return CommonResult.failed("删除缓存失败: " + e.getMessage());
        }
    }

    /**
     * 检查缓存是否存在
     */
    @GetMapping("/exists/{key}")
    public CommonResult<Boolean> existsCache(@PathVariable String key) {
        try {
            boolean exists = cacheManager.exists(key);
            return CommonResult.success(exists, "检查缓存成功");
        } catch (Exception e) {
            return CommonResult.failed("检查缓存失败: " + e.getMessage());
        }
    }

    /**
     * 获取缓存过期时间
     */
    @GetMapping("/expire/{key}")
    public CommonResult<Long> getCacheExpire(@PathVariable String key) {
        try {
            Long expire = cacheManager.getExpire(key);
            return CommonResult.success(expire, "获取过期时间成功");
        } catch (Exception e) {
            return CommonResult.failed("获取过期时间失败: " + e.getMessage());
        }
    }
} 