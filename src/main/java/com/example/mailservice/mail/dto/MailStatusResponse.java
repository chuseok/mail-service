package com.example.mailservice.mail.dto;

import com.example.mailservice.mail.model.MailRequest;

import java.time.LocalDateTime;

public record MailStatusResponse(
        String requestId,
        String toEmail,
        String subject,
        String status,
        int retryCount,
        int maxRetry,
        LocalDateTime nextRetryAt,
        String lastErrorMessage
) {
    public static MailStatusResponse from(MailRequest request) {
        return new MailStatusResponse(
                request.getRequestId(),
                request.getToEmail(),
                request.getSubject(),
                request.getStatus().name(),
                request.getRetryCount(),
                request.getMaxRetry(),
                request.getNextRetryAt(),
                request.getLastErrorMessage()
        );
    }
}