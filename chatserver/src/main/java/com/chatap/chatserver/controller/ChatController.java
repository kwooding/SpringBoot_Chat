package com.chatapp.chatserver.controller;

import com.chatapp.chatserver.model.ChatMessage;
import com.chatapp.chatserver.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.time.Instant;

@Controller
public class ChatController{
    private final MessageService messageService;
    
    public ChatController(MessageService messageService){
        this.messageService = messageService;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage send(ChatMessage message){
        ChatMessage stamped = new ChatMessage(message.sender(), message.content(), Instant.now());

        messageService.save(stamped);
        return stamped;
    }



}