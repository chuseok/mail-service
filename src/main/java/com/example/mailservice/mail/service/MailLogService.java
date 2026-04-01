package com.example.mailservice.mail.service;

import com.example.mailservice.mail.model.MailLog;
import com.example.mailservice.mail.model.MailLogEventType;
import com.example.mailservice.mail.repository.MailLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MailLogService {

    private final MailLogRepository mailLogRepository;

    public void log(String requestId, MailLogEventType eventType, String message) {
        mailLogRepository.save(requestId, eventType, message);
    }

    public List<MailLog> getLogs(String requestId) {
        return mailLogRepository.findByRequestId(requestId);
    }
}