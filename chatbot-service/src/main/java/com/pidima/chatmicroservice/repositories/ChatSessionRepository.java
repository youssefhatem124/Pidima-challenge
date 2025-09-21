package com.pidima.chatmicroservice.repositories;

import com.pidima.chatmicroservice.models.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, String> {
}