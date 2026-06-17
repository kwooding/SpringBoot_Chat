package com.chatapp.chatserver.repository;

import com.chatapp.chatserver.model.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;



public interface MessageRepository extends JpaRepository<MessageEntity, Long>{
    List<MessageEntity> findAllByOrderByTimestampDesc();
    Page<MessageEntity> findAllByOrderByTimestampDesc(Pageable pageable);
}