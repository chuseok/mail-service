package com.example.mailservice.mail.repository;

import com.example.mailservice.mail.model.MailRequest;
import com.example.mailservice.mail.model.MailStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MailRequestRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<MailRequest> mailRequestRowMapper = (rs, rowNum) -> {
        MailRequest request = new MailRequest();
        request.setRequestId(rs.getString("request_id"));
        request.setToEmail(rs.getString("to_email"));
        request.setSubject(rs.getString("subject"));
        request.setBody(rs.getString("body"));
        request.setStatus(MailStatus.valueOf(rs.getString("status")));
        request.setRetryCount(rs.getInt("retry_count"));
        request.setMaxRetry(rs.getInt("max_retry"));
        request.setLastErrorMessage(rs.getString("last_error_message"));

        Timestamp nextRetryAt = rs.getTimestamp("next_retry_at");
        if (nextRetryAt != null) {
            request.setNextRetryAt(nextRetryAt.toLocalDateTime());
        }

        return request;
    };

    public void save(MailRequest request) {
        String sql = """
                INSERT INTO mail_request
                (request_id, to_email, subject, body, status, retry_count, max_retry, last_error_message, next_retry_at, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
                """;

        jdbcTemplate.update(
                sql,
                request.getRequestId(),
                request.getToEmail(),
                request.getSubject(),
                request.getBody(),
                request.getStatus().name(),
                request.getRetryCount(),
                request.getMaxRetry(),
                request.getLastErrorMessage(),
                request.getNextRetryAt()
        );
    }

    public MailRequest findByRequestId(String requestId) {
        String sql = """
                SELECT request_id, to_email, subject, body, status, retry_count, max_retry,
                       last_error_message, next_retry_at
                FROM mail_request
                WHERE request_id = ?
                """;

        return jdbcTemplate.query(
                sql,
                mailRequestRowMapper,
                requestId
        ).stream().findFirst().orElse(null);
    }

    public void updateStatus(String requestId, MailStatus status) {
        String sql = """
                UPDATE mail_request
                SET status = ?, updated_at = NOW()
                WHERE request_id = ?
                """;

        jdbcTemplate.update(sql, status.name(), requestId);
    }

    public void markAsSent(String requestId) {
        String sql = """
        UPDATE mail_request
        SET status = ?, updated_at = NOW(), last_error_message = NULL, next_retry_at = NULL
        WHERE request_id = ?
        """;

        jdbcTemplate.update(sql, MailStatus.SENT.name(), requestId);
    }

    public void markForRetry(String requestId, int retryCount, LocalDateTime nextRetryAt, String lastErrorMessage) {
        String sql = """
        UPDATE mail_request
        SET status = ?, retry_count = ?, next_retry_at = ?, last_error_message = ?, updated_at = NOW()
        WHERE request_id = ?
        """;

        jdbcTemplate.update(
                sql,
                MailStatus.RETRY_WAIT.name(),
                retryCount,
                nextRetryAt,
                lastErrorMessage,
                requestId
        );
    }

    public void markAsFailed(String requestId, String lastErrorMessage) {
        String sql = """
        UPDATE mail_request
        SET status = ?, last_error_message = ?, updated_at = NOW()
        WHERE request_id = ?
        """;

        jdbcTemplate.update(
                sql,
                MailStatus.FAILED.name(),
                lastErrorMessage,
                requestId
        );
    }

    public List<MailRequest> findRecoverableRequests() {
        String sql = """
        SELECT request_id, to_email, subject, body, status, retry_count, max_retry, next_retry_at, last_error_message
        FROM mail_request
        WHERE status = ?
           OR status = ?
           OR status = ?
        ORDER BY updated_at ASC
        """;

        return jdbcTemplate.query(
                sql,
                mailRequestRowMapper,
                MailStatus.PENDING.name(),
                MailStatus.PROCESSING.name(),
                MailStatus.RETRY_WAIT.name()
        );
    }
}