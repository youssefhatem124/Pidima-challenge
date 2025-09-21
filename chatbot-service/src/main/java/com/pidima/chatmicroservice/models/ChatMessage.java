package com.pidima.chatmicroservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Data
@NoArgsConstructor
public class ChatMessage {
    @Id
    private String messageId;
    
    @Column(name = "session_id", nullable = false)
    private String sessionId;
    
    @Column(name = "content", nullable = false, length = 1000)
    private String content;
    
    @Column(name = "sender", nullable = false)
    private String sender;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    public ChatMessage(String sessionId, String content, String sender) {
        this.sessionId = sessionId;
        this.content = content;
        this.sender = sender;
        this.timestamp = LocalDateTime.now();
    }
    
    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}