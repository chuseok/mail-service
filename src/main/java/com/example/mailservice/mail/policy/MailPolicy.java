package com.example.mailservice.mail.policy;

public record MailPolicy(
        String customerCode,
        int maxRetry,
        int retryIntervalSeconds,
        String fromEmail
) {
}