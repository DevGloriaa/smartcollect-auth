package com.example.smartcollectauth.service;

import com.example.smartcollectauth.dto.LoginDto;
import com.example.smartcollectauth.dto.UserRegistrationRequest;

import java.util.Map;

public interface UserService {

    Map<String, Object> registration(UserRegistrationRequest request);

    boolean verifyOtp(String email, String otpCode);

    boolean resendOtp(String email);

    String login(LoginDto loginDto);
}
