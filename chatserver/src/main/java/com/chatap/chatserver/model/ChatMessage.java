package com.chatapp.chatserver.model;
import java.time.Instant;

public record ChatMessage(
    String sender,
    String content,
    Instant timestamp
){}