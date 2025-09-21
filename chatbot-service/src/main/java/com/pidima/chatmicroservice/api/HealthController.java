package com.pidima.chatmicroservice.api;

import com.pidima.chatmicroservice.services.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {
    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    @Autowired
    private ChatService chatService;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        logger.debug("Health check requested");
        
        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("status", "UP");
        healthStatus.put("timestamp", LocalDateTime.now());
        healthStatus.put("service", "chat-microservice");
        healthStatus.put("version", "0.0.1-SNAPSHOT");
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("active_sessions", chatService.getSessionCount());
        healthStatus.put("metrics", metrics);
        
        logger.debug("Health check completed successfully");
        return ResponseEntity.ok(healthStatus);
    }
}