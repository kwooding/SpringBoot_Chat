package com.chatapp.chatserver.config;

import com.chatapp.chatserver.websocket.ChatWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/* This File is now depreciated and no longer used do to the implemenation leading to the refactoring of the code using STOMP to pipeline the data
throughout the web socket connection*/

// @Configuration
// @EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{

    private final ChatWebSocketHandler chatWebSocketHandler;
    public WebSocketConfig(ChatWebSocketHandler chatWebSocketHandler){
        this.chatWebSocketHandler = chatWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry){
        registry.addHandler(chatWebSocketHandler, "/chat").setAllowedOrigins("*");
    }
}