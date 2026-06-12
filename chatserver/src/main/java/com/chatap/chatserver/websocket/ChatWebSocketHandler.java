package com.chatapp.chatserver.websocket;

import com.chatapp.chatserver.model.ChatMessage;
import com.chatapp.chatserver.model.ClientConnection;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.CloseStatus;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler{
    // Use this instead of normal list as to not throw concurrent modification exception
    private final List<ClientConnection> clients = new CopyOnWriteArrayList<>();

    

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        //adding a session thread to our list of sessions and prinitng out its connection id
        ClientConnection client = new ClientConnection(session,Instant.now());
        clients.add(client);

        System.out.println("Connected: " + session.getId() + " at " + client.getConnectedAt()) ;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        // for each client we want to iterate thorugh our list of clients get their session and then send a message to that session
        
        for(ClientConnection c : clients){
            WebSocketSession target = c.getSession();
            if(target.isOpen()){
                target.sendMessage(message);
            }
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        boolean removed = clients.removeIf(client -> 
                                    client.getSession()
                                    .getId()
                                    .equals(session.getId()));

        if (removed){
            System.out.println("Removed: " + session.getId());
        }
    }
}