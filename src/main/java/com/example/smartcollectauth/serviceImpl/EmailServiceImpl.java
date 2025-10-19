package com.example.smartcollectauth.serviceImpl;

import com.example.smartcollectauth.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendOtpEmail(String toEmail, String otpCode) {
        String subject = "SmartCollect Account Verification OTP";
        String body = "Hello 👋,\n\n"
                + "Your SmartCollect verification code is: " + otpCode + "\n\n"
                + "This code will expire in 10 minutes.\n"
                + "If you didn’t request this, please ignore this email.\n\n"
                + "Best regards,\n"
                + "SmartCollect Team";

        sendEmail(toEmail, subject, body);
    }

    @Override
    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
