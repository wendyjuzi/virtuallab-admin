package com.edu.virtuallab.monitor.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Set;

@Component
public class UserSessionCache {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String ONLINE_USERS_KEY = "online_users";

    public int getOnlineUserCount() {
        Set<String> keys = redisTemplate.keys(ONLINE_USERS_KEY + ":*");
        return keys != null ? keys.size() : 0;
    }

    public void markOnline(String userId) {
        redisTemplate.opsForValue().set(ONLINE_USERS_KEY + ":" + userId, true, Duration.ofMinutes(30));
    }

    public void logout(String userId) {
        redisTemplate.delete(ONLINE_USERS_KEY + ":" + userId);
    }
}