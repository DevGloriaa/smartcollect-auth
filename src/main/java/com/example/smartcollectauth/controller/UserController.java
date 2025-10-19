package com.example.smartcollectauth.controller;

import com.example.smartcollectauth.dto.LoginDto;
import com.example.smartcollectauth.dto.UserRegistrationRequest;
import com.example.smartcollectauth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserRegistrationRequest request) {
        Map<String, Object> response = userService.registration(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean verified = userService.verifyOtp(email, otp);
        return ResponseEntity.ok(Map.of(
                "success", verified,
                "message", verified ? "OTP verified successfully!" : "Invalid or expired OTP."
        ));
    }


    @PostMapping("/resend-otp")
    public ResponseEntity<Map<String, Object>> resendOtp(@RequestParam String email) {
        boolean sent = userService.resendOtp(email);
        return ResponseEntity.ok(Map.of(
                "success", sent,
                "message", sent ? "OTP resent successfully!" : "Failed to resend OTP. Please try again."
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody LoginDto loginDto) {
        String token = userService.login(loginDto);
        return ResponseEntity.ok(Map.of(
                "success", token != null,
                "token", token,
                "message", token != null ? "Login successful." : "Invalid credentials."
        ));
    }
}
