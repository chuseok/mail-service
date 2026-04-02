package com.example.mailservice.mail.service;

import com.example.mailservice.mail.dto.MailSendRequest;
import com.example.mailservice.mail.dto.MailSendResponse;
import com.example.mailservice.mail.model.MailLogEventType;
import com.example.mailservice.mail.model.MailRequest;
import com.example.mailservice.mail.model.MailStatus;
import com.example.mailservice.mail.policy.MailPolicy;
import com.example.mailservice.mail.queue.MailQueue;
import com.example.mailservice.mail.repository.MailRequestRepository;
import com.example.mailservice.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailRequestService {
    /*
    requestId 생성
    메일 요청 객체 생성
    DB 저장
    큐 넣기
     */
    private final MailRequestRepository mailRequestRepository;
    private final MailQueue mailQueue;
    private final MailLogService mailLogService;
    private final DefaultMailPolicyService mailPolicyService;

    @Transactional
    public MailSendResponse createMailRequest(MailSendRequest dto) {
        String requestId = IdGenerator.generateRequestId();

        MailPolicy policy = mailPolicyService.getPolicy(dto.customerCode());

        MailRequest request = new MailRequest();
        request.setRequestId(requestId);
        request.setCustomerCode(policy.customerCode());
        request.setToEmail(dto.to());
        request.setSubject(dto.subject());
        request.setBody(dto.body());
        request.setStatus(MailStatus.PENDING);
        request.setRetryCount(0);
        request.setMaxRetry(3);

        log.info("mail request created. requestId={}, customerCode={}, to={}",
                requestId, policy.customerCode(), dto.to());
        mailLogService.log(
                requestId,
                MailLogEventType.REQUEST_CREATED,
                "mail request created"
        );

        mailRequestRepository.save(request);
        log.info("mail request saved. requestId={}, status={}",
                requestId, request.getStatus());
        mailLogService.log(
                requestId,
                MailLogEventType.REQUEST_SAVED,
                "mail request saved with status=PENDING"
        );
        mailQueue.enqueue(requestId);
        log.info("mail enqueued. requestId={}", requestId);
        mailLogService.log(
                requestId,
                MailLogEventType.ENQUEUED,
                "request enqueued for async processing"
        );

        return new MailSendResponse(requestId, "ACCEPTED");
    }
}