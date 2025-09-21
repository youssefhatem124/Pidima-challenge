package com.pidima.chatmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSessionResponse {
    private String sessionId;
    private LocalDateTime createdAt;
}