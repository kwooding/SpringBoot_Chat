package com.chatapp.chatserver.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PresenceService{
    //Redis key for our set of online users
    private static final String ONLINE_USERS_KEY = "online-users";

    

    private final StringRedisTemplate redisTemplate;

    public PresenceService(StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public void userOnline(String username){
        redisTemplate.opsForSet().add(ONLINE_USERS_KEY, username);
    }

    public void userOffline(String username){
        redisTemplate.opsForSet().remove(ONLINE_USERS_KEY, username);
    }

    public Set<String> getOnlineUsers(){
        return redisTemplate.opsForSet().members(ONLINE_USERS_KEY);
    }
}