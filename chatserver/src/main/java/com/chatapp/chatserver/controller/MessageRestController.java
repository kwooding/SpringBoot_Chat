package com.chatapp.chatserver.controller;

import com.chatapp.chatserver.model.ChatMessage;
import com.chatapp.chatserver.service.MessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
public class MessageRestController{
    private final MessageService messageService;

    public MessageRestController(MessageService messageService){
        this.messageService = messageService;
    }

    @GetMapping("/api/messages")
    public List<ChatMessage> getMessages(@RequestParam(defaultValue = "0")int page,@RequestParam(defaultValue = "20") int size){
        return messageService.getMessagePage(page,size);
    }

    @DeleteMapping("/api/messages/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id){
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}