package com.example.mailservice.mail.service;

import com.example.mailservice.config.MailConfig;
import com.example.mailservice.mail.exception.MailSendException;
import com.example.mailservice.mail.model.MailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSendService {
    /*
    SMTP 연결
    메일 발송
     */
    private final JavaMailSender javaMailSender;
    private final MailConfig mailConfig;

    public void send(MailRequest request) {
        if (request.getToEmail().contains("fail")) {
            throw new RuntimeException("forced failure for test");
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailConfig.getFromAddress());
        message.setTo(request.getToEmail());
        message.setSubject(request.getSubject());
        message.setText(request.getBody());

        try {
            javaMailSender.send(message);
        } catch (org.springframework.mail.MailException e) {
            log.error("메일 발송 실패 requestId={}", request.getRequestId(), e);
            throw new MailSendException("메일 발송 실패", e);
        }
    }
}
