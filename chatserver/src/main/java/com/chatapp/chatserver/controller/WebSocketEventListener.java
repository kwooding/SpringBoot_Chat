package com.chatapp.chatserver.controller;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import com.chatapp.chatserver.model.ChatMessage;
import com.chatapp.chatserver.service.PresenceService;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import java.time.Instant;

import java.security.Principal;

@Component
public class WebSocketEventListener {
    private final SimpMessagingTemplate template;
    private final PresenceService presenceService;

    public WebSocketEventListener(SimpMessagingTemplate template, PresenceService presenceService){
        this.template = template;
        this.presenceService = presenceService;
    }

    // Marks joins and takes them as an event turned to a server connection string and this string is then converted to a JSON output
    @EventListener
    public void handleConnect(SessionConnectEvent event){
        Principal user = event.getUser();
        if(user == null){
            return;
        }

        String username = user.getName();

        presenceService.userOnline(username);

        template.convertAndSend("/topic/presence", presenceService.getOnlineUsers());
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event){
        Principal user = event.getUser();

        if(user == null){
            return;
        }

        String username = user.getName();

        presenceService.userOffline(username);

        template.convertAndSend("/topic/presence", presenceService.getOnlineUsers());

    }


}