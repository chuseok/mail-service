package com.example.mailservice.mail.service;

import com.example.mailservice.mail.model.MailRequest;
import com.example.mailservice.mail.model.MailStatus;
import com.example.mailservice.mail.queue.MailQueue;
import com.example.mailservice.mail.repository.MailRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailRecoveryService {

    private final MailRequestRepository mailRequestRepository;
    private final MailQueue mailQueue;

    public void recoverPendingJobs() {
        List<MailRequest> targets = mailRequestRepository.findRecoverableRequests();

        log.info("recoverable mail requests found. count={}", targets.size());

        for (MailRequest request : targets) {
            if (request.getStatus() == MailStatus.RETRY_WAIT && request.getNextRetryAt() != null) {
                mailQueue.enqueue(request.getRequestId(), request.getNextRetryAt());
                log.info("recovered retry request. requestId={}, nextRetryAt={}",
                        request.getRequestId(), request.getNextRetryAt());
            } else {
                mailQueue.enqueue(request.getRequestId());
                log.info("recovered immediate request. requestId={}, status={}",
                        request.getRequestId(), request.getStatus());
            }
        }
    }
}