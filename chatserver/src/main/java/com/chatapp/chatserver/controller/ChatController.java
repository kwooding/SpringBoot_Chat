package com.chatapp.chatserver.controller;

import com.chatapp.chatserver.model.ChatMessage;
import com.chatapp.chatserver.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.security.Principal;
import java.time.Instant;

import com.chatapp.chatserver.service.RedisPublisher;

@Controller
public class ChatController{
    private final MessageService messageService;
    private final RedisPublisher redisPublisher;
    
    public ChatController(MessageService messageService, RedisPublisher redisPublisher){
        this.messageService = messageService;
        this.redisPublisher = redisPublisher;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public void send(ChatMessage message, Principal principal){

        //Using principal.getName instead of message.sender to make sure names are server enforced
        ChatMessage stamped = new ChatMessage(principal.getName(), message.content(), Instant.now());

        messageService.save(stamped);

        String jsonMessage = messageService.toJson(stamped);

        redisPublisher.publish(jsonMessage);
        
        // we post to redis now so there is no need for our SendTo to return
        //return stamped;
    }



}