package com.example.mailservice.mail.queue;

import java.time.LocalDateTime;
import java.util.concurrent.DelayQueue;

public class InMemoryMailQueue implements MailQueue {
    /*
    Blocking 구현
     */
    private final DelayQueue<DelayedMailTask> queue = new DelayQueue<>();

    @Override
    public void enqueue(String requestId) {
        queue.offer(new DelayedMailTask(requestId, LocalDateTime.now()));
    }
    @Override
    public void enqueue(String requestId, LocalDateTime availableAt) {
        LocalDateTime scheduledAt = availableAt == null ? LocalDateTime.now() : availableAt;
        queue.offer(new DelayedMailTask(requestId, scheduledAt));
    }

    @Override
    public String dequeue() throws InterruptedException {
        return queue.take().getRequestId();
    }

    @Override
    public int size() {
        return queue.size();
    }
}
