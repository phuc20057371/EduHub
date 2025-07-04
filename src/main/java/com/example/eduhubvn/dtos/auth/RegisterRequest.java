package com.example.eduhubvn.dtos.auth;


import com.example.eduhubvn.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String phone;
    private String email;
    private String password;
    private Role role;
    private String otp;
}
