package com.chatapp.chatserver.websocket;

import com.chatapp.chatserver.model.ChatMessage;
import com.chatapp.chatserver.model.ClientConnection;
import com.chatapp.chatserver.service.MessageService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.CloseStatus;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler{
    // Use this instead of normal list as to not throw concurrent modification exception
    private final List<ClientConnection> clients = new CopyOnWriteArrayList<>();
    private final static Logger log = LoggerFactory.getLogger(ChatWebSocketHandler.class);

    private final MessageService messageService;
    public ChatWebSocketHandler (MessageService messageService){
        this.messageService = messageService;
    }

    

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        //adding a session thread to our list of sessions and prinitng out its connection id
        ClientConnection client = new ClientConnection(session,Instant.now());
        clients.add(client);

        log.info("Connected: {} at {}", session.getId(),client.getConnectedAt());

        ChatMessage sysMessage = messageService.systemMessage("User " + session.getId() +  " has connected");
        String m = messageService.toJson(sysMessage);
        broadcast(new TextMessage(m));


    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        // for each client we want to iterate thorugh our list of clients get their session and then send a message to that session
        String m = message.getPayload();

    
        ChatMessage cm = messageService.parseIncoming(m);

        if (cm == null){

            ChatMessage err = messageService.systemMessage("Error sending Message");
            String j = messageService.toJson(err);
            session.sendMessage(new TextMessage(j));
            return;
        }
         

        String jsonMessage = messageService.toJson(cm);

        broadcast(new TextMessage(jsonMessage));

        



    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        boolean removed = clients.removeIf(client -> 
                                    client.getSession()
                                    .getId()
                                    .equals(session.getId()));

        if (removed){
            ChatMessage sysMessage = messageService.systemMessage("User " + session.getId() + " has been removed");
            String m = messageService.toJson(sysMessage);
            broadcast(new TextMessage(m));

            log.info("Removed: {}" , session.getId());
        }

    }

    private void broadcast(TextMessage message) {
        for (ClientConnection c : clients){
            if (c.getSession().isOpen()){
                try{
                    c.getSession().sendMessage(message);
                }
                catch(IOException e){
                    log.error("Message " + message + " is invalid");
                    
                }

            }
        }
    }
}