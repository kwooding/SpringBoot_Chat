package com.chatapp.chatserver.controller;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import com.chatapp.chatserver.model.ChatMessage;
import java.time.Instant;

@Component
public class WebSocketEventListener {
    private final SimpMessagingTemplate template;

    public WebSocketEventListener(SimpMessagingTemplate template){
        this.template = template;
    }

    // Marks joins and takes them as an event turned to a server connection string and this string is then converted to a JSON output
    @EventListener
    public void handleConnect(SessionConnectEvent event){
        String sessionId = event.getMessage().getHeaders().get("simpSessionId").toString();
        ChatMessage msg = new ChatMessage("SERVER", "User " + sessionId + " has connected",Instant.now());
        template.convertAndSend("/topic/messages",msg);
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event){
        String sessionId = event.getSessionId();
        ChatMessage msg = new ChatMessage("SERVER", "User " + sessionId + " has disconnected", Instant.now());
        template.convertAndSend("/topic/messages", msg);
    }


}