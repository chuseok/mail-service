package com.example.mailservice.mail.model;

import java.time.LocalDateTime;

public class MailRequest {
    private String requestId;
    private String toEmail;
    private String subject;
    private String body;
    private MailStatus status;
    private int retryCount;
    private int maxRetry;
    private LocalDateTime nextRetryAt;
    private String lastErrorMessage;

    public MailRequest() {}

    public MailRequest(String requestId, String toEmail, String subject, String body) {
        this.requestId = requestId;
        this.toEmail = toEmail;
        this.subject = subject;
        this.body = body;
        this.status = MailStatus.PENDING;
        this.retryCount = 0;
        this.maxRetry = 3;
    }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public String getToEmail() { return toEmail; }
    public void setToEmail(String toEmail) { this.toEmail = toEmail; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public MailStatus getStatus() { return status; }
    public void setStatus(MailStatus status) { this.status = status; }

    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }

    public int getMaxRetry() { return maxRetry; }
    public void setMaxRetry(int maxRetry) { this.maxRetry = maxRetry; }

    public LocalDateTime getNextRetryAt() { return nextRetryAt; }
    public void setNextRetryAt(LocalDateTime nextRetryAt) { this.nextRetryAt = nextRetryAt; }

    public String getLastErrorMessage() { return lastErrorMessage; }
    public void setLastErrorMessage(String lastErrorMessage) { this.lastErrorMessage = lastErrorMessage; }
}