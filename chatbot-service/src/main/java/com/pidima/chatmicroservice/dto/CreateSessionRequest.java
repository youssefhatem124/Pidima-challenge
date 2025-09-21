package com.pidima.chatmicroservice.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSessionRequest {
    
    @Size(max = 100, message = "Initial message cannot exceed 100 characters")
    private String initialMessage;
}