package com.example.smartcollectauth.service;

import java.util.Map;

public interface UserService {
    Map<String, Object> registration(UserRegistrationRequest request);
    boolean verifyOtp(String email, String otpCode);
    String login(LoginDto loginDto);
    boolean resendOtp(String email);

}