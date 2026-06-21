package com.chatapp.chatserver.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisHealthCheck implements CommandLineRunner{
    private final StringRedisTemplate redisTemplate;

    // This is a spring helper that helps maintian reads and writes to the cache
    public RedisHealthCheck(StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    // This tests reads and writes by writing a message, reading it and printing it out if this
    // cycle completes than redis is running properly
    @Override
    public void run(String... args) {
        redisTemplate.opsForValue().set("healthcheck", "redis is connected");
        String value = redisTemplate.opsForValue().get("healthcheck");
        System.out.println(">>> REDIS TEST: " + value);
    }
}