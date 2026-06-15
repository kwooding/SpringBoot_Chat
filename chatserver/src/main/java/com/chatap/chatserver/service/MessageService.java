package com.chatapp.chatserver.service;

import com.chatapp.chatserver.model.ChatMessage;
import tools.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MessageService{
    private static final Logger log = LoggerFactory.getLogger(MessageService.class);
    private final ObjectMapper objectMapper;

    public MessageService(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }


    public ChatMessage parseIncoming(String raw){

        try{
            ChatMessage incoming = objectMapper.readValue(raw, ChatMessage.class);
            return new ChatMessage(incoming.sender(), incoming.content(),Instant.now());

        }catch(JacksonException e){
            log.error("Failed to parse incoming JSON {}", e.getMessage());

            return null;
        }
    }

    public String toJson(ChatMessage message){
        try{
            return objectMapper.writeValueAsString(message);
            
        }catch(JacksonException e){
            log.error("Error converting message to JSON {}", e.getMessage());
            return null;
        }
    }

    public ChatMessage systemMessage(String message){
        return new ChatMessage("SERVER", message, Instant.now());
    }
        
}