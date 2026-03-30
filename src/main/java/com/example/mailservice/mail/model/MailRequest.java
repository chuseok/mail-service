package com.example.mailservice.mail.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailRequest {
    private String requestId;
    private String toEmail;
    private String subject;
    private String body;
    private MailStatus status;
    private int retryCount;
    private int maxRetry;
    private LocalDateTime nextRetryAt;
    private String lastErrorMessage;

    public MailRequest(String requestId, String toEmail, String subject, String body) {
        this.requestId = requestId;
        this.toEmail = toEmail;
        this.subject = subject;
        this.body = body;
        this.status = MailStatus.PENDING;
        this.retryCount = 0;
        this.maxRetry = 3;
    }
}