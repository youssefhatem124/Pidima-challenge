package com.pidima.chatmicroservice.repositories;

import com.pidima.chatmicroservice.models.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.sessionId = :sessionId ORDER BY cm.timestamp ASC")
    List<ChatMessage> findBySessionIdOrderByTimestampAsc(@Param("sessionId") String sessionId);
}