package com.example.mailservice.mail.service;

import com.example.mailservice.mail.dto.MailSendRequest;
import com.example.mailservice.mail.dto.MailSendResponse;
import com.example.mailservice.mail.model.MailRequest;
import com.example.mailservice.mail.model.MailStatus;
import com.example.mailservice.mail.repository.MailRequestRepository;
import com.example.mailservice.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailRequestService {
    /*
    requestId 생성
    메일 요청 객체 생성
    DB 저장
    큐 넣기
     */
        private final MailRequestRepository mailRequestRepository;

        public MailSendResponse createMailRequest(MailSendRequest dto) {
            String requestId = IdGenerator.generateRequestId();

            MailRequest request = new MailRequest();
            request.setRequestId(requestId);
            request.setToEmail(dto.to());
            request.setSubject(dto.subject());
            request.setBody(dto.body());
            request.setStatus(MailStatus.PENDING);
            request.setRetryCount(0);
            request.setMaxRetry(3);

            mailRequestRepository.save(request);

            return new MailSendResponse(requestId, "ACCEPTED");
        }
    }