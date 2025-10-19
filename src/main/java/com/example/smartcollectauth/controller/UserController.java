package com.example.smartcollectauth.controller;

import com.example.smartcollectauth.dto.LoginDto;
import com.example.smartcollectauth.dto.OtpVerificationRequest;
import com.example.smartcollectauth.dto.UserRegistrationRequest;
import com.example.smartcollectauth.dto.LoginRequest;
import com.example.smartcollectauth.dto.VerifyOtpRequest;
import com.example.smartcollectauth.model.User;
import com.example.smartcollectauth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest request) {
        String response = userService.register(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationRequest request) {
        String response = userService.verifyOtp(request);
        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDto request) {
        String response = userService.login(request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }
}
