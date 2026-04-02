package com.example.mailservice.mail.service;

import com.example.mailservice.mail.model.MailRequest;
import com.example.mailservice.mail.repository.MailRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailQueryService {

    private final MailRequestRepository mailRequestRepository;

    public MailRequest getByRequestId(String requestId) {
        MailRequest request = mailRequestRepository.findByRequestId(requestId);
        if (request == null) {
            throw new IllegalArgumentException("mail request not found. requestId=" + requestId);
        }
        return request;
    }
}