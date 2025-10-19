package com.example.smartcollectauth.serviceImpl;

import com.example.smartcollectauth.dto.LoginDto;
import com.example.smartcollectauth.dto.UserRegistrationRequest;
import com.example.smartcollectauth.model.User;
import com.example.smartcollectauth.repository.UserRepository;
import com.example.smartcollectauth.service.EmailService;
import com.example.smartcollectauth.service.OtpService;
import com.example.smartcollectauth.service.UserService;
import com.example.smartcollectauth.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    private final Map<String, UserRegistrationRequest> pendingUsers = new HashMap<>();
    private final Map<String, String> pendingOtps = new HashMap<>();

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           OtpService otpService,
                           EmailService emailService,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Map<String, Object> registration(UserRegistrationRequest request) {
        Map<String, Object> response = new HashMap<>();

        if (request.getUsername() == null || request.getEmail() == null || request.getPassword() == null) {
            response.put("success", false);
            response.put("message", "All fields are required");
            return response;
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            response.put("success", false);
            response.put("message", "Email already registered");
            return response;
        }


        String otp = otpService.generateOtp(request.getEmail());
        pendingUsers.put(request.getEmail(), request);
        pendingOtps.put(request.getEmail(), otp);

        emailService.sendOtpEmail(request.getEmail(), otp);

        response.put("success", true);
        response.put("message", "OTP sent to email. Complete verification to register.");
        return response;
    }

    @Override
    public boolean verifyOtp(String email, String otpCode) {
        if (!pendingOtps.containsKey(email)) {
            throw new RuntimeException("No pending registration found for this email!");
        }

        if (!pendingOtps.get(email).equals(otpCode)) {
            throw new RuntimeException("Invalid OTP");
        }

        UserRegistrationRequest pending = pendingUsers.get(email);

        User user = new User();
        user.setUsername(pending.getUsername());
        user.setEmail(pending.getEmail());
        user.setPassword(passwordEncoder.encode(pending.getPassword()));
        user.setVerified(true);

        userRepository.save(user);

        pendingUsers.remove(email);
        pendingOtps.remove(email);

        return true;
    }

    @Override
    public boolean resendOtp(String email) {
        if (!pendingUsers.containsKey(email)) {
            throw new RuntimeException("No pending registration found for this email!");
        }

        String otp = otpService.generateOtp(email);
        pendingOtps.put(email, otp);
        emailService.sendOtpEmail(email, otp);

        return true;
    }

    @Override
    public String login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        if (!user.isVerified()) {
            throw new RuntimeException("User email not verified!");
        }

        boolean passwordMatches = passwordEncoder.matches(loginDto.getPassword(), user.getPassword());
        if (!passwordMatches) {
            throw new RuntimeException("Wrong username or password");
        }

        return jwtUtil.generateToken(user.getEmail());
    }
}
