package com.example.mailservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class MailConfig {

    @Value("${spring.mail.username}")
    private String fromAddress;
}