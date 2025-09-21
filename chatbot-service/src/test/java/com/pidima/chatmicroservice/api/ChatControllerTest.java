package com.pidima.chatmicroservice.api;

import com.pidima.chatmicroservice.dto.CreateSessionRequest;
import com.pidima.chatmicroservice.dto.SendMessageRequest;
import com.pidima.chatmicroservice.models.ChatMessage;
import com.pidima.chatmicroservice.models.ChatSession;
import com.pidima.chatmicroservice.services.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createSession_Success() throws Exception {
        // Given
        CreateSessionRequest request = new CreateSessionRequest("Hello world");
        ChatSession mockSession = new ChatSession();
        mockSession.setSessionId("test-session-id");
        mockSession.setCreatedAt(LocalDateTime.now());

        when(chatService.createSession(anyString())).thenReturn(mockSession);

        // When & Then
        mockMvc.perform(post("/chat/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.session_id").value("test-session-id"))
                .andExpect(jsonPath("$.created_at").exists());
    }

    @Test
    void createSession_ValidationError() throws Exception {
        // Given - request with message too long
        CreateSessionRequest request = new CreateSessionRequest("a".repeat(101));

        // When & Then
        mockMvc.perform(post("/chat/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.validation_errors.initialMessage").exists());
    }

    @Test
    void sendMessage_Success() throws Exception {
        // Given
        SendMessageRequest request = new SendMessageRequest("session-123", "Hello", "John");
        ChatMessage mockMessage = new ChatMessage("session-123", "Hello", "John");
        mockMessage.setMessageId("msg-123");

        when(chatService.sendMessage(anyString(), anyString(), anyString())).thenReturn(mockMessage);

        // When & Then
        mockMvc.perform(post("/chat/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message_id").value("msg-123"))
                .andExpect(jsonPath("$.session_id").value("session-123"))
                .andExpect(jsonPath("$.content").value("Hello"))
                .andExpect(jsonPath("$.sender").value("John"));
    }

    @Test
    void sendMessage_ValidationError_EmptyContent() throws Exception {
        // Given - request with empty content
        SendMessageRequest request = new SendMessageRequest("session-123", "", "John");

        // When & Then
        mockMvc.perform(post("/chat/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.validation_errors.content").exists());
    }

    @Test
    void sendMessage_ValidationError_MissingSender() throws Exception {
        // Given - request without sender
        SendMessageRequest request = new SendMessageRequest("session-123", "Hello", null);

        // When & Then
        mockMvc.perform(post("/chat/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.validation_errors.sender").exists());
    }

    @Test
    void getChatHistory_Success() throws Exception {
        // Given
        String sessionId = "session-123";
        List<ChatMessage> mockMessages = Arrays.asList(
                new ChatMessage(sessionId, "Hello", "John"),
                new ChatMessage(sessionId, "Hi there!", "Jane")
        );

        when(chatService.getChatHistory(sessionId)).thenReturn(mockMessages);

        // When & Then
        mockMvc.perform(get("/chat/history/" + sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].content").value("Hello"))
                .andExpect(jsonPath("$[0].sender").value("John"))
                .andExpect(jsonPath("$[1].content").value("Hi there!"))
                .andExpect(jsonPath("$[1].sender").value("Jane"));
    }

    @Test
    void getChatHistory_SessionNotFound() throws Exception {
        // Given
        String sessionId = "non-existent-session";
        when(chatService.getChatHistory(sessionId))
                .thenThrow(new IllegalArgumentException("Session not found: " + sessionId));

        // When & Then
        mockMvc.perform(get("/chat/history/" + sessionId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Session not found: " + sessionId));
    }
}