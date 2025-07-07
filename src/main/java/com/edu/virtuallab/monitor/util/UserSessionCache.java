package com.edu.virtuallab.monitor.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class UserSessionCache {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String ONLINE_USERS_KEY = "online_users";
    private static final String USER_ACTIVITY_KEY = "user_activity";
    private static final String USER_SESSION_KEY = "user_session";

    /**
     * 获取在线用户数量
     */
    public int getOnlineUserCount() {
        Set<String> keys = redisTemplate.keys(ONLINE_USERS_KEY + ":*");
        return keys != null ? keys.size() : 0;
    }

    /**
     * 标记用户在线
     */
    public void markOnline(String userId) {
        String onlineKey = ONLINE_USERS_KEY + ":" + userId;
        String activityKey = USER_ACTIVITY_KEY + ":" + userId;
        
        // 设置在线状态，30分钟过期
        redisTemplate.opsForValue().set(onlineKey, true, Duration.ofMinutes(30));
        
        // 记录用户活动时间
        redisTemplate.opsForValue().set(activityKey, System.currentTimeMillis(), Duration.ofMinutes(30));
    }

    /**
     * 更新用户活动时间
     */
    public void updateUserActivity(String userId) {
        String onlineKey = ONLINE_USERS_KEY + ":" + userId;
        String activityKey = USER_ACTIVITY_KEY + ":" + userId;
        
        // 延长在线状态
        redisTemplate.expire(onlineKey, Duration.ofMinutes(30));
        
        // 更新活动时间
        redisTemplate.opsForValue().set(activityKey, System.currentTimeMillis(), Duration.ofMinutes(30));
    }

    /**
     * 用户登出
     */
    public void logout(String userId) {
        String onlineKey = ONLINE_USERS_KEY + ":" + userId;
        String activityKey = USER_ACTIVITY_KEY + ":" + userId;
        String sessionKey = USER_SESSION_KEY + ":" + userId;
        
        redisTemplate.delete(onlineKey);
        redisTemplate.delete(activityKey);
        redisTemplate.delete(sessionKey);
    }

    /**
     * 检查用户是否在线
     */
    public boolean isUserOnline(String userId) {
        String onlineKey = ONLINE_USERS_KEY + ":" + userId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(onlineKey));
    }

    /**
     * 获取用户最后活动时间
     */
    public Long getUserLastActivity(String userId) {
        String activityKey = USER_ACTIVITY_KEY + ":" + userId;
        Object value = redisTemplate.opsForValue().get(activityKey);
        return value instanceof Long ? (Long) value : null;
    }

    /**
     * 设置用户会话信息
     */
    public void setUserSession(String userId, Object sessionData, Duration duration) {
        String sessionKey = USER_SESSION_KEY + ":" + userId;
        redisTemplate.opsForValue().set(sessionKey, sessionData, duration);
    }

    /**
     * 获取用户会话信息
     */
    public Object getUserSession(String userId) {
        String sessionKey = USER_SESSION_KEY + ":" + userId;
        return redisTemplate.opsForValue().get(sessionKey);
    }

    /**
     * 获取所有在线用户ID
     */
    public Set<String> getOnlineUserIds() {
        Set<String> keys = redisTemplate.keys(ONLINE_USERS_KEY + ":*");
        if (keys != null) {
            return keys.stream()
                    .map(key -> key.substring(ONLINE_USERS_KEY.length() + 1))
                    .collect(java.util.stream.Collectors.toSet());
        }
        return java.util.Collections.emptySet();
    }

    /**
     * 清理过期会话
     */
    public void cleanupExpiredSessions() {
        // Redis会自动清理过期的key，这里可以添加额外的清理逻辑
        Set<String> onlineKeys = redisTemplate.keys(ONLINE_USERS_KEY + ":*");
        if (onlineKeys != null) {
            for (String key : onlineKeys) {
                // 检查是否真的过期
                Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
                if (expire != null && expire <= 0) {
                    redisTemplate.delete(key);
                }
            }
        }
    }

    /**
     * 强制用户下线
     */
    public void forceLogout(String userId) {
        logout(userId);
    }

    /**
     * 批量强制用户下线
     */
    public void forceLogoutBatch(Set<String> userIds) {
        for (String userId : userIds) {
            forceLogout(userId);
        }
    }

    /**
     * 获取用户会话统计信息
     */
    public SessionStatistics getSessionStatistics() {
        SessionStatistics stats = new SessionStatistics();
        
        Set<String> onlineKeys = redisTemplate.keys(ONLINE_USERS_KEY + ":*");
        Set<String> activityKeys = redisTemplate.keys(USER_ACTIVITY_KEY + ":*");
        Set<String> sessionKeys = redisTemplate.keys(USER_SESSION_KEY + ":*");
        
        stats.setOnlineUserCount(onlineKeys != null ? onlineKeys.size() : 0);
        stats.setActiveUserCount(activityKeys != null ? activityKeys.size() : 0);
        stats.setSessionCount(sessionKeys != null ? sessionKeys.size() : 0);
        
        return stats;
    }

    /**
     * 会话统计信息
     */
    public static class SessionStatistics {
        private int onlineUserCount;
        private int activeUserCount;
        private int sessionCount;

        public int getOnlineUserCount() { return onlineUserCount; }
        public void setOnlineUserCount(int onlineUserCount) { this.onlineUserCount = onlineUserCount; }
        public int getActiveUserCount() { return activeUserCount; }
        public void setActiveUserCount(int activeUserCount) { this.activeUserCount = activeUserCount; }
        public int getSessionCount() { return sessionCount; }
        public void setSessionCount(int sessionCount) { this.sessionCount = sessionCount; }
    }
}