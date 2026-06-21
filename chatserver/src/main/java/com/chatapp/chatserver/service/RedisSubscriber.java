package com.chatapp.chatserver.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Service
public class RedisSubscriber{
    private final SimpMessagingTemplate messagingTemplate;

    public RedisSubscriber(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate = messagingTemplate;
    }

    //converts json message into readable template and makes it available
    public void onMessage(String message){
        messagingTemplate.convertAndSend("/topic/messages", message);
    }
}