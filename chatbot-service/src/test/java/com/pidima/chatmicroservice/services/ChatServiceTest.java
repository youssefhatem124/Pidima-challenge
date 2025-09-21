package com.pidima.chatmicroservice.services;

import com.pidima.chatmicroservice.models.ChatMessage;
import com.pidima.chatmicroservice.models.ChatSession;
import com.pidima.chatmicroservice.repositories.ChatMessageRepository;
import com.pidima.chatmicroservice.repositories.ChatSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatSessionRepository sessionRepository;

    @Mock
    private ChatMessageRepository messageRepository;

    @InjectMocks
    private ChatService chatService;

    private ChatSession testSession;

    @BeforeEach
    void setUp() {
        testSession = new ChatSession();
        testSession.setSessionId("test-session-id");
    }

    @Test
    void createSession_WithInitialMessage() {
        // Given
        String initialMessage = "Hello, this is the first message";
        when(sessionRepository.save(any(ChatSession.class))).thenReturn(testSession);
        when(messageRepository.save(any(ChatMessage.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ChatSession session = chatService.createSession(initialMessage);

        // Then
        assertNotNull(session);
        assertEquals("test-session-id", session.getSessionId());
        verify(sessionRepository).save(any(ChatSession.class));
        verify(messageRepository).save(any(ChatMessage.class));
    }

    @Test
    void createSession_WithoutInitialMessage() {
        // Given
        when(sessionRepository.save(any(ChatSession.class))).thenReturn(testSession);

        // When
        ChatSession session = chatService.createSession(null);

        // Then
        assertNotNull(session);
        assertEquals("test-session-id", session.getSessionId());
        verify(sessionRepository).save(any(ChatSession.class));
        verify(messageRepository, never()).save(any(ChatMessage.class));
    }

    @Test
    void sendMessage_Success() {
        // Given
        String sessionId = "test-session-id";
        String content = "Hello World";
        String sender = "John";
        
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(testSession));
        when(messageRepository.save(any(ChatMessage.class))).thenAnswer(invocation -> {
            ChatMessage message = invocation.getArgument(0);
            message.setMessageId("test-message-id");
            return message;
        });

        // When
        ChatMessage message = chatService.sendMessage(sessionId, content, sender);

        // Then
        assertNotNull(message);
        assertEquals("test-message-id", message.getMessageId());
        assertEquals(sessionId, message.getSessionId());
        assertEquals(content, message.getContent());
        assertEquals(sender, message.getSender());
        assertNotNull(message.getTimestamp());
        verify(sessionRepository).findById(sessionId);
        verify(messageRepository).save(any(ChatMessage.class));
    }

    @Test
    void sendMessage_SessionNotFound() {
        // Given
        String nonExistentSessionId = "non-existent-session";
        when(sessionRepository.findById(nonExistentSessionId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> chatService.sendMessage(nonExistentSessionId, "Hello", "John")
        );

        assertEquals("Session not found: " + nonExistentSessionId, exception.getMessage());
        verify(sessionRepository).findById(nonExistentSessionId);
        verify(messageRepository, never()).save(any(ChatMessage.class));
    }

    @Test
    void getChatHistory_Success() {
        // Given
        String sessionId = "test-session-id";
        List<ChatMessage> messages = new ArrayList<>();
        
        ChatMessage msg1 = new ChatMessage(sessionId, "Initial message", "system");
        msg1.setMessageId("msg1");
        ChatMessage msg2 = new ChatMessage(sessionId, "Second message", "User");
        msg2.setMessageId("msg2");
        ChatMessage msg3 = new ChatMessage(sessionId, "Third message", "Assistant");
        msg3.setMessageId("msg3");
        
        messages.add(msg1);
        messages.add(msg2);
        messages.add(msg3);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(testSession));
        when(messageRepository.findBySessionIdOrderByTimestampAsc(sessionId)).thenReturn(messages);

        // When
        List<ChatMessage> history = chatService.getChatHistory(sessionId);

        // Then
        assertEquals(3, history.size());
        assertEquals("Initial message", history.get(0).getContent());
        assertEquals("system", history.get(0).getSender());
        assertEquals("Second message", history.get(1).getContent());
        assertEquals("User", history.get(1).getSender());
        assertEquals("Third message", history.get(2).getContent());
        assertEquals("Assistant", history.get(2).getSender());
        verify(sessionRepository).findById(sessionId);
        verify(messageRepository).findBySessionIdOrderByTimestampAsc(sessionId);
    }

    @Test
    void getChatHistory_SessionNotFound() {
        // Given
        String nonExistentSessionId = "non-existent-session";
        when(sessionRepository.findById(nonExistentSessionId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> chatService.getChatHistory(nonExistentSessionId)
        );

        assertEquals("Session not found: " + nonExistentSessionId, exception.getMessage());
        verify(sessionRepository).findById(nonExistentSessionId);
        verify(messageRepository, never()).findBySessionIdOrderByTimestampAsc(anyString());
    }

    @Test
    void getSession_Found() {
        // Given
        String sessionId = "test-session-id";
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(testSession));

        // When
        var result = chatService.getSession(sessionId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(sessionId, result.get().getSessionId());
        verify(sessionRepository).findById(sessionId);
    }

    @Test
    void getSession_NotFound() {
        // Given
        String sessionId = "non-existent-session";
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // When
        var result = chatService.getSession(sessionId);

        // Then
        assertFalse(result.isPresent());
        verify(sessionRepository).findById(sessionId);
    }

    @Test
    void getSessionCount() {
        // Given
        when(sessionRepository.count()).thenReturn(5L);

        // When
        int count = chatService.getSessionCount();

        // Then
        assertEquals(5, count);
        verify(sessionRepository).count();
    }
}