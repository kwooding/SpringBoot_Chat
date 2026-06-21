package com.chatapp.chatserver.service;

import com.chatapp.chatserver.config.RedisConfig;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher{

    private final StringRedisTemplate redisTemplate;

    public RedisPublisher(StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }
    // Publishing to redis converting the message, similar to SimpConvertAndSend but doesnt publish to user
    public void publish(String jsonMessage){
        redisTemplate.convertAndSend(RedisConfig.CHANNEL, jsonMessage);
    }

}