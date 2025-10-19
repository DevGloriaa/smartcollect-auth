package com.example.smartcollectauth.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    private String fullName;
    private String email;
    private String password;
    private String otpCode;
}
