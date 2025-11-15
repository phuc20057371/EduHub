package com.example.eduhubvn.dtos.auth.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordReq {
    private String email;
    private String otp;
    private String newPassword;
}
