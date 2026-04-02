package com.example.mailservice.mail.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MailLog {
    private Long id;
    private String requestId;
    private MailLogEventType eventType;
    private String message;
    private LocalDateTime createdAt;
}