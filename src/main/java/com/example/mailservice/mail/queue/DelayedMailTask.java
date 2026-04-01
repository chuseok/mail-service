package com.example.mailservice.mail.queue;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayedMailTask implements Delayed {
    private final String requestId;
    private final LocalDateTime availableAt;

    public DelayedMailTask(String requestId, LocalDateTime availableAt) {
        this.requestId = Objects.requireNonNull(requestId, "requestId must not be null");
        this.availableAt = Objects.requireNonNullElseGet(availableAt, LocalDateTime::now);
    }

    public String getRequestId() {
        return requestId;
    }
    public LocalDateTime getAvailableAt() {
        return availableAt;
    }
    @Override
    public long getDelay(TimeUnit unit) {
        long millis = Duration.between(LocalDateTime.now(), availableAt).toMillis();
        return unit.convert(millis, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed other) {
        if (other == this) {
            return 0;
        }

        long diff = this.getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS);
        return Long.compare(diff, 0);
    }
}
