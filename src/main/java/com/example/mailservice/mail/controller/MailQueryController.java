package com.example.mailservice.mail.controller;

import com.example.mailservice.mail.dto.MailLogResponse;
import com.example.mailservice.mail.dto.MailStatusResponse;
import com.example.mailservice.mail.service.MailLogService;
import com.example.mailservice.mail.service.MailQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailQueryController {

    private final MailQueryService mailQueryService;
    private final MailLogService mailLogService;

    @GetMapping("/{requestId}")
    public MailStatusResponse getStatus(@PathVariable String requestId) {
        return MailStatusResponse.from(mailQueryService.getByRequestId(requestId));
    }

    @GetMapping("/{requestId}/logs")
    public List<MailLogResponse> getLogs(@PathVariable String requestId) {
        return mailLogService.getLogs(requestId)
                .stream()
                .map(MailLogResponse::from)
                .toList();
    }
}