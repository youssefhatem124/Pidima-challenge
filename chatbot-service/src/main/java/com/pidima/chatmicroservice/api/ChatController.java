package com.pidima.chatmicroservice.api;

import com.pidima.chatmicroservice.dto.CreateSessionRequest;
import com.pidima.chatmicroservice.dto.CreateSessionResponse;
import com.pidima.chatmicroservice.dto.SendMessageRequest;
import com.pidima.chatmicroservice.dto.SendMessageResponse;
import com.pidima.chatmicroservice.models.ChatMessage;
import com.pidima.chatmicroservice.models.ChatSession;
import com.pidima.chatmicroservice.services.ChatService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatService chatService;

    @PostMapping("/session")
    public ResponseEntity<CreateSessionResponse> createSession(@Valid @RequestBody CreateSessionRequest request) {
        logger.info("Received request to create new chat session");
        
        ChatSession session = chatService.createSession(request.getInitialMessage());
        
        CreateSessionResponse response = new CreateSessionResponse(
                session.getSessionId(),
                session.getCreatedAt()
        );
        
        logger.info("Successfully created session with ID: {}", session.getSessionId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/message")
    public ResponseEntity<SendMessageResponse> sendMessage(@Valid @RequestBody SendMessageRequest request) {
        logger.info("Received request to send message to session: {}", request.getSessionId());
        
        ChatMessage message = chatService.sendMessage(
                request.getSessionId(),
                request.getContent(),
                request.getSender()
        );
        
        SendMessageResponse response = new SendMessageResponse(
                message.getMessageId(),
                message.getSessionId(),
                message.getContent(),
                message.getSender(),
                message.getTimestamp()
        );
        
        logger.info("Successfully sent message with ID: {}", message.getMessageId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/history/{sessionId}")
    public ResponseEntity<List<ChatMessage>> getChatHistory(@PathVariable String sessionId) {
        logger.info("Received request to get chat history for session: {}", sessionId);
        
        List<ChatMessage> messages = chatService.getChatHistory(sessionId);
        
        logger.info("Successfully retrieved {} messages for session: {}", messages.size(), sessionId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}