package com.chatapp.chatserver.repository;

import com.chatapp.chatserver.model.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface MessageRepository extends JpaRepository<MessageEntity, Long>{
    List<MessageEntity> findAllByOrderByTimestampDesc();
}