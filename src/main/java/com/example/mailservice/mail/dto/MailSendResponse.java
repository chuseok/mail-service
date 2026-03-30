package com.example.mailservice.mail.dto;

public record MailSendResponse(
        String requestId,
        String status
) {
}