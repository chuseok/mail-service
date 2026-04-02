package com.example.mailservice.mail.model;

public enum MailLogEventType {
    REQUEST_CREATED,
    REQUEST_SAVED,
    ENQUEUED,
    WORKER_PICKED,
    PROCESSING_STARTED,
    SEND_ATTEMPT,
    SEND_SUCCESS,
    SEND_FAIL,
    RETRY_SCHEDULED,
    RECOVERED_ON_STARTUP,
    FINAL_FAIL,
    ALREADY_SENT_SKIPPED
}