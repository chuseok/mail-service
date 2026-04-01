package com.example.mailservice.mail.queue;

import java.time.LocalDateTime;

public interface MailQueue {
    void enqueue(String requestId);
    void enqueue(String requestId, LocalDateTime availableAt);
    String dequeue() throws InterruptedException;
    int size();
}