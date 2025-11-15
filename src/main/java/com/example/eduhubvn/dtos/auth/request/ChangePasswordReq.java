package com.example.eduhubvn.dtos.auth.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordReq {
    private String email;
    private String otp;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}