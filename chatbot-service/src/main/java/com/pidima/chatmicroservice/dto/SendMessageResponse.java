package com.pidima.chatmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageResponse {
    private String messageId;
    private String sessionId;
    private String content;
    private String sender;
    private LocalDateTime timestamp;
}