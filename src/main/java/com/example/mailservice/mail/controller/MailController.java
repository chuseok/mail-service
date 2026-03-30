package com.example.mailservice.mail.controller;

import com.example.mailservice.mail.dto.MailSendRequest;
import com.example.mailservice.mail.dto.MailSendResponse;
import com.example.mailservice.mail.service.MailRequestService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailRequestService mailRequestService;

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public MailSendResponse send(@Valid @RequestBody MailSendRequest request) {
        return mailRequestService.createMailRequest(request);
    }
}