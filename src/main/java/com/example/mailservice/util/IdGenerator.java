package com.example.mailservice.util;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@NoArgsConstructor
public final class IdGenerator {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static String generateRequestId() {
        return "MAIL-" +
                LocalDateTime.now().format(FORMATTER) +
                "-" +
                UUID.randomUUID().toString().substring(0, 8);
    }
}