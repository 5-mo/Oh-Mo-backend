package com.example.ohmobackend.service.emailService;

public interface EmailService {
    void sendVerificationCode(String toEmail, String code);
}
