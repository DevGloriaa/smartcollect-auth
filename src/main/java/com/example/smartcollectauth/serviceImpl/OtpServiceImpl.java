package com.example.smartcollectauth.serviceImpl;

import com.example.smartcollectauth.service.EmailService;
import com.example.smartcollectauth.service.OtpService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {

    private final EmailService emailService;
    private final Map<String, OtpData> otpStore = new HashMap<>();
    private static final int OTP_EXPIRATION_MINUTES = 5;

    public OtpServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public String generateOtp(String email) {
        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(email, new OtpData(otp, LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES)));

        // HTML email content
        String subject = "🔒 SmartCollect Verification Code";
        String htmlMessage = "<html>"
                + "<body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333;'>"
                + "<h2 style='color: #4CAF50;'>Hello 👋</h2>"
                + "<p>Here is your verification code for <strong>SmartCollect</strong>:</p>"
                + "<h1 style='background-color: #f0f0f0; padding: 10px; display: inline-block; border-radius: 5px;'>"
                + otp + "</h1>"
                + "<p>This code will expire in <strong>" + OTP_EXPIRATION_MINUTES + " minutes</strong>.</p>"
                + "<p style='font-size: 0.9em; color: #777;'>If you didn’t request this, please ignore this email.</p>"
                + "<br>"
                + "<p>Best regards,<br><strong>SmartCollect Team</strong></p>"
                + "</body>"
                + "</html>";

        // Send HTML email
        emailService.sendHtmlEmail(email, subject, htmlMessage);

        return otp;
    }

    @Override
    public boolean validateOtp(String email, String otp) {
        OtpData otpData = otpStore.get(email);

        if (otpData == null) return false;

        if (otpData.expirationTime.isBefore(LocalDateTime.now())) {
            otpStore.remove(email);
            return false;
        }

        boolean isValid = otpData.otp.equals(otp);
        if (isValid) otpStore.remove(email);
        return isValid;
    }

    @Override
    public void clearOtp(String email) {
        otpStore.remove(email);
    }

    private static class OtpData {
        String otp;
        LocalDateTime expirationTime;

        OtpData(String otp, LocalDateTime expirationTime) {
            this.otp = otp;
            this.expirationTime = expirationTime;
        }
    }
}
