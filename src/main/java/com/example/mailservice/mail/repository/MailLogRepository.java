package com.example.mailservice.mail.repository;

import com.example.mailservice.mail.model.MailLog;
import com.example.mailservice.mail.model.MailLogEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MailLogRepository {

    private final JdbcTemplate jdbcTemplate;

    public void save(String requestId, MailLogEventType eventType, String message) {
        String sql = """
            INSERT INTO mail_log (request_id, event_type, message)
            VALUES (?, ?, ?)
            """;

        jdbcTemplate.update(
                sql,
                requestId,
                eventType.name(),
                message
        );
    }

    public List<MailLog> findByRequestId(String requestId) {
        String sql = """
            SELECT id, request_id, event_type, message, created_at
            FROM mail_log
            WHERE request_id = ?
            ORDER BY created_at ASC, id ASC
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            MailLog log = new MailLog();
            log.setId(rs.getLong("id"));
            log.setRequestId(rs.getString("request_id"));
            log.setEventType(MailLogEventType.valueOf(rs.getString("event_type")));
            log.setMessage(rs.getString("message"));

            Timestamp createdAt = rs.getTimestamp("created_at");
            if (createdAt != null) {
                log.setCreatedAt(createdAt.toLocalDateTime());
            }

            return log;
        }, requestId);
    }
}