package com.example.mailservice.mail.policy;

public interface MailPolicyService {
    MailPolicy getPolicy(String customerCode);
}