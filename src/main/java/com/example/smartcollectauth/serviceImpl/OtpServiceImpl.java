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
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(email, new OtpData(otp, LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES)));

        String subject = "SmartCollect Verification Code";
        String message = "Your verification code is: " + otp + "\nIt expires in 5 minutes.";
        emailService.sendEmail(email, subject, message);

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
