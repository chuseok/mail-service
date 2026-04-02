package com.example.mailservice.mail.dto;

import com.example.mailservice.mail.model.MailLog;

import java.time.LocalDateTime;

public record MailLogResponse(
        Long id,
        String requestId,
        String eventType,
        String message,
        LocalDateTime createdAt
) {
    public static MailLogResponse from(MailLog log) {
        return new MailLogResponse(
                log.getId(),
                log.getRequestId(),
                log.getEventType().name(),
                log.getMessage(),
                log.getCreatedAt()
        );
    }
}