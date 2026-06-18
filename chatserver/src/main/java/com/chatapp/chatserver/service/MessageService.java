package com.chatapp.chatserver.service;

import com.chatapp.chatserver.model.ChatMessage;
import tools.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.chatapp.chatserver.model.MessageEntity;
import com.chatapp.chatserver.repository.MessageRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

@Service
public class MessageService{
    private static final Logger log = LoggerFactory.getLogger(MessageService.class);
    private final ObjectMapper objectMapper;
    private final MessageRepository messageRepository;

    public MessageService(ObjectMapper objectMapper,MessageRepository messageRepository){
        this.objectMapper = objectMapper;
        this.messageRepository = messageRepository;
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

    public MessageEntity save(ChatMessage message){
        MessageEntity m = new MessageEntity(message.sender(),message.content(), message.timestamp());
        return messageRepository.save(m);
    }


    //Used for saving message data using Transactional to ensure clean reads
    //Uses streams to take in all messages ordered by timesamp to map new chatmessages into the list
    @Transactional(readOnly = true)
    public List<ChatMessage> getRecentMessages(){
        return messageRepository.findAllByOrderByTimestampDesc()
                                .stream()
                                .map(entity -> new ChatMessage(entity.getSender(),entity.getContent(),entity.getTimestamp()))
                                .toList();
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> getMessagePage(int page, int size){
        Pageable p = PageRequest.of(page,size);
        Page<MessageEntity> p_list = messageRepository.findAllByOrderByTimestampDesc(p);
        return p_list.getContent()
                    .stream()
                    .map(entity -> new ChatMessage(entity.getSender(),entity.getContent(),entity.getTimestamp()))
                    .toList();

    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteMessage(Long id){
        messageRepository.deleteById(id);
    }
        
}