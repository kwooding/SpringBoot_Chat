package com.chatapp.chatserver.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService{


    //number of messages allowed per time window
    private static final int MAX_MESSAGES = 10;

    // size of window in seconds

    private static final int WINDOW_SECONDS = 10;

    private final StringRedisTemplate redisTemplate;

    public RateLimitService(StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String username){
        String key = "rate: " + username;

        Long count = redisTemplate.opsForValue().increment(key);

        if(count != null && count == 1){
            redisTemplate.expire(key, Duration.ofSeconds(WINDOW_SECONDS));
        }

        return count != null && count <= MAX_MESSAGES;
    }
}