package com.example.mailservice.mail.exception;

public class MailSendException extends RuntimeException {
    public MailSendException(String message, Throwable cuase) {
        super(message, cuase);
    }
}
