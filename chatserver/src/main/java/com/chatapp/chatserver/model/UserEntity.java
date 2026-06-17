package com.chatapp.chatserver.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import java.time.Instant;

@Entity
@Table(name = "users")
public class UserEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true, length = 50)
    private String username;


    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false, length = 255)
    private String roles;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;


    protected UserEntity(){

    }

    public UserEntity(String username, String passwordHash, String roles, Instant createdAt){
        this.username = username;
        this.passwordHash = passwordHash;
        this.roles = roles;
        this.createdAt = createdAt;
    }

    public Long getId(){
        return id;
    }
    public String getUsername(){
        return username;
    }

    public String getPasswordHash(){
        return passwordHash;
    }

    public String getRoles(){
        return roles;
    }

    public Instant getCreatedAt(){
        return createdAt;
    }


}