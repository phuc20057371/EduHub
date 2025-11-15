package com.example.eduhubvn.dtos.auth.request;


import com.example.eduhubvn.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterReq {
    private String phone;
    private String email;
    private String password;
    private Role role;
    private String otp;
}
