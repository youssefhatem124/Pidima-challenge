package com.pidima.chatmicroservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "chat_sessions")
@Data
@NoArgsConstructor
public class ChatSession {
    @Id
    private String sessionId;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "sessionId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("timestamp ASC")
    private List<ChatMessage> messages;

    public ChatSession(String sessionId) {
        this.sessionId = sessionId;
        this.createdAt = LocalDateTime.now();
        this.messages = new ArrayList<>();
    }
    
    @PrePersist
    protected void onCreate() {
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (messages == null) {
            messages = new ArrayList<>();
        }
    }
}