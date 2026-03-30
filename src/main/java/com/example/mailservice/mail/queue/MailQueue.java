package com.example.mailservice.mail.queue;

public interface MailQueue {
    void enqueue(String requestId) throws InterruptedException;
    String dequeue() throws InterruptedException;
}