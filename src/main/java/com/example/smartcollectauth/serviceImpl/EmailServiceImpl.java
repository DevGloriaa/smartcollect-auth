package com.example.smartcollectauth.serviceImpl;

import com.example.smartcollectauth.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
        String subject = "🔒 SmartCollect Account Verification OTP";

        String body = "<html>"
                + "<body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333;'>"
                + "<h2 style='color: #4CAF50;'>Hello 👋</h2>"
                + "<p>Thank you for choosing <strong>SmartCollect</strong>! Your verification code is:</p>"
                + "<h1 style='background-color: #f0f0f0; padding: 10px; display: inline-block; border-radius: 5px;'>"
                + otpCode + "</h1>"
                + "<p>This code will expire in <strong>10 minutes</strong>.</p>"
                + "<p style='font-size: 0.9em; color: #777;'>If you didn’t request this, please ignore this email.</p>"
                + "<br>"
                + "<p>Best regards,<br><strong>SmartCollect Team</strong></p>"
                + "</body>"
                + "</html>";

        sendHtmlEmail(toEmail, subject, body);
    }

    @Override
    public void sendHtmlEmail(String toEmail, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
