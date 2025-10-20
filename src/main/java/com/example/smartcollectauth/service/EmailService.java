package com.example.smartcollectauth.service;

public interface EmailService {


    void sendOtpEmail(String toEmail, String otpCode);


    void sendHtmlEmail(String toEmail, String subject, String htmlBody);
}
