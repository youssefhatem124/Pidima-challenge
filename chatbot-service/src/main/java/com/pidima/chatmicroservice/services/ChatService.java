package com.pidima.chatmicroservice.services;

import com.pidima.chatmicroservice.models.ChatMessage;
import com.pidima.chatmicroservice.models.ChatSession;
import com.pidima.chatmicroservice.repositories.ChatMessageRepository;
import com.pidima.chatmicroservice.repositories.ChatSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChatService {
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    
    @Autowired
    private ChatSessionRepository sessionRepository;
    
    @Autowired
    private ChatMessageRepository messageRepository;

    @Transactional
    public ChatSession createSession(String initialMessage) {

        logger.info("Creating new chat session with initial message: {}", 
                    initialMessage != null && !initialMessage.isEmpty() ? "provided" : "none");
        
        ChatSession session = new ChatSession();
        session = sessionRepository.save(session);
        
        // Add initial message if provided
        if (initialMessage != null && !initialMessage.trim().isEmpty()) {
            ChatMessage message = new ChatMessage(session.getSessionId(), initialMessage, "system");
            message.setMessageId(UUID.randomUUID().toString());
            messageRepository.save(message);
        }
        
        logger.info("Created chat session with ID: {}", session.getSessionId());
        
        return session;
    }

    @Transactional
    public ChatMessage sendMessage(String sessionId, String content, String sender) {
        logger.info("Sending message to session: {} from sender: {}", sessionId, sender);
        
        Optional<ChatSession> sessionOpt = sessionRepository.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            logger.warn("Session not found: {}", sessionId);
            throw new IllegalArgumentException("Session not found: " + sessionId);
        }

        ChatMessage message = new ChatMessage(sessionId, content, sender);
        message.setMessageId(UUID.randomUUID().toString());
        
        message = messageRepository.save(message);
        
        logger.info("Message sent successfully with ID: {}", message.getMessageId());
        return message;
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> getChatHistory(String sessionId) {
        logger.info("Retrieving chat history for session: {}", sessionId);
        
        Optional<ChatSession> sessionOpt = sessionRepository.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            logger.warn("Session not found: {}", sessionId);
            throw new IllegalArgumentException("Session not found: " + sessionId);
        }

        List<ChatMessage> messages = messageRepository.findBySessionIdOrderByTimestampAsc(sessionId);
        logger.info("Retrieved {} messages for session: {}", messages.size(), sessionId);
        
        return messages;
    }

    @Transactional(readOnly = true)
    public Optional<ChatSession> getSession(String sessionId) {
        return sessionRepository.findById(sessionId);
    }

    @Transactional(readOnly = true)
    public int getSessionCount() {
        return (int) sessionRepository.count();
    }
}