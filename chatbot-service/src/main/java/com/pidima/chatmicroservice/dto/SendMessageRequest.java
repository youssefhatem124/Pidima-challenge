package com.pidima.chatmicroservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {
    
    @NotBlank(message = "Session ID is required")
    private String sessionId;
    
    @NotBlank(message = "Message content is required")
    @Size(max = 500, message = "Message content cannot exceed 500 characters")
    private String content;
    
    @NotBlank(message = "Sender is required")
    @Size(max = 50, message = "Sender name cannot exceed 50 characters")
    private String sender;
}