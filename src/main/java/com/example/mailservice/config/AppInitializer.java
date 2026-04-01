package com.example.mailservice.config;

import com.example.mailservice.mail.service.MailRecoveryService;
import com.example.mailservice.mail.worker.MailWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppInitializer {
    /*
    서버 시작 시
    Queue 생성
    Worker 시작
    RetryScheduler 시작
     */
    private final MailWorker mailWorker;

    private final MailRecoveryService mailRecoveryService;

    @EventListener(ApplicationReadyEvent.class)
    public void startWorker() {
        log.info("mail system initialization started.");
        mailRecoveryService.recoverPendingJobs();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(mailWorker);
        log.info("mail worker started.");
    }
}