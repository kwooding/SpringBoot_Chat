package com.chatapp.chatserver.controller;

import com.chatapp.chatserver.model.ChatMessage;
import com.chatapp.chatserver.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.security.Principal;
import java.time.Instant;

@Controller
public class ChatController{
    private final MessageService messageService;
    
    public ChatController(MessageService messageService){
        this.messageService = messageService;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage send(ChatMessage message, Principal principal){

        //Using principal.getName instead of message.sender to make sure names are server enforced
        ChatMessage stamped = new ChatMessage(principal.getName(), message.content(), Instant.now());

        messageService.save(stamped);
        return stamped;
    }



}