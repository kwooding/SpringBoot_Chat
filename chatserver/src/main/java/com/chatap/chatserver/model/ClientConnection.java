package com.chatapp.chatserver.model;

import java.time.Instant;
import org.springframework.web.socket.WebSocketSession;

public class ClientConnection{
    private final WebSocketSession session;
    private final Instant connectedAt;

    public ClientConnection(WebSocketSession session, Instant connectedAt){
        this.session= session;
        this.connectedAt= connectedAt;
    }

    public WebSocketSession getSession(){
        return session;
    }

    public Instant getConnectedAt(){
        return connectedAt;
    }
}