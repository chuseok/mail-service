package com.example.mailservice.mail.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MailSendRequest(
        @NotBlank(message = "수신자 이메일은 필수입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String to,

        @NotBlank(message = "제목은 필수입니다.")
        String subject,

        @NotBlank(message = "본문은 필수입니다.")
        String body
) {
}