package com.example.smartcollectauth.service;

public interface EmailService {


    void sendOtpEmail(String toEmail, String otpCode);

    void sendEmail(String toEmail, String subject, String body);
}
