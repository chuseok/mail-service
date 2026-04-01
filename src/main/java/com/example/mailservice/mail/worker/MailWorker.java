package com.example.mailservice.mail.worker;

import com.example.mailservice.mail.model.MailRequest;
import com.example.mailservice.mail.model.MailStatus;
import com.example.mailservice.mail.queue.MailQueue;
import com.example.mailservice.mail.repository.MailRequestRepository;
import com.example.mailservice.mail.service.MailSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailWorker implements Runnable {

    private final MailQueue mailQueue;
    private final MailRequestRepository mailRequestRepository;
    private final MailSendService mailSenderService;

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                String requestId = mailQueue.dequeue();
                log.info("worker picked message from queue. requestId={}", requestId);
                process(requestId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info("mail worker interrupted. stopping worker.");
                break;
            } catch (Exception e) {
                log.error("unexpected error in mail worker loop", e);
            }
        }
    }

    private void process(String requestId) {
        MailRequest request = mailRequestRepository.findByRequestId(requestId);

        if (request == null) {
            log.warn("mail request not found. requestId={}", requestId);
            return;
        }

        if (request.getStatus() == MailStatus.SENT) {
            log.info("already sent. skipping requestId={}", requestId);
            return;
        }

        mailRequestRepository.updateStatus(requestId, MailStatus.PROCESSING);

        try {
            log.info("mail send attempt. requestId={}, to={}",
                    requestId, request.getToEmail());
            mailSenderService.send(request);

            mailRequestRepository.markAsSent(requestId);
            log.info("mail sent successfully. requestId={}", requestId);

        } catch (Exception e) {
            handleFailure(request, e);
        }
    }

    private void handleFailure(MailRequest request, Exception e) {
        int nextRetryCount = request.getRetryCount() + 1;
        String requestId = request.getRequestId();

        log.warn("mail failure handled. requestId={}, nextRetryCount={}, maxRetry={}",
                requestId, nextRetryCount, request.getMaxRetry());
        if (nextRetryCount >= request.getMaxRetry()) {
            mailRequestRepository.markAsFailed(
                    requestId,
                    e.getMessage()
            );
            log.error("mail failed permanently. requestId={}, retryCount={}, error={}",
                    requestId, nextRetryCount, e.getMessage(), e);
            return;
        }

        LocalDateTime nextRetryAt = LocalDateTime.now().plusMinutes(nextRetryCount);

        mailRequestRepository.markForRetry(
                requestId,
                nextRetryCount,
                nextRetryAt,
                e.getMessage()
        );

        mailQueue.enqueue(requestId, nextRetryAt);
        log.error("mail failed permanently. requestId={}, retryCount={}, error={}",
                requestId, nextRetryCount, e.getMessage(), e);
    }
}