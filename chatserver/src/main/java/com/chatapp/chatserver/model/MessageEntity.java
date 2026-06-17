package com.chatapp.chatserver.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import java.time.Instant;

@Entity
@Table(name= "messages")
public class MessageEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // The following three groups are setting up the values for our SQL table setting each column
    @Column(nullable = false, length = 100)
    private String sender;

    @Column(nullable = false)
    private String content;


    @Column(nullable = false)
    private Instant timestamp;

    protected MessageEntity(){

    }

    public MessageEntity(String sender, String content, Instant timestamp){
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Long getId(){
        return id;
    }

    public String getSender(){
        return sender;
    }

    public String getContent(){
        return content;
    }

    public Instant getTimestamp(){
        return timestamp;
    }



}