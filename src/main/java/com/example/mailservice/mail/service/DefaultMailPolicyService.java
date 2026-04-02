package com.example.mailservice.mail.service;

import com.example.mailservice.mail.policy.MailPolicy;
import com.example.mailservice.mail.policy.MailPolicyService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultMailPolicyService implements MailPolicyService {

    private final Map<String, MailPolicy> policyMap = Map.of(
            "DEFAULT", new MailPolicy("DEFAULT", 3, 60, "noreply@default.com"),
            "VIP", new MailPolicy("VIP", 5, 30, "noreply@vip.com"),
            "INTERNAL", new MailPolicy("INTERNAL", 2, 10, "noreply@internal.com")
    );

    @Override
    public MailPolicy getPolicy(String customerCode) {
        if (customerCode == null || customerCode.isBlank()) {
            return policyMap.get("DEFAULT");
        }
        return policyMap.getOrDefault(customerCode, policyMap.get("DEFAULT"));
    }
}