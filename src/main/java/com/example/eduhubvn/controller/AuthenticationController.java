package com.example.eduhubvn.controller;

import com.example.eduhubvn.dtos.AuthenResponse;
import com.example.eduhubvn.dtos.Email;
import com.example.eduhubvn.dtos.LoginRequest;
import com.example.eduhubvn.dtos.RegisterRequest;
import com.example.eduhubvn.services.AuthenticationService;
import com.example.eduhubvn.services.OtpService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final OtpService otpService;

    @PostMapping("/register")
    public ResponseEntity<AuthenResponse> register(
            @RequestBody RegisterRequest request
    ){
        if (!otpService.validate(request.getEmail(), request.getOtp())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(authenticationService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenResponse> login(
            @RequestBody LoginRequest request
    ){
        return ResponseEntity.ok(authenticationService.login(request));
    }
    @PostMapping("/refresh")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(
            @RequestBody Email email
    ) throws MessagingException {
        return ResponseEntity.ok(otpService.sendEmail(email.getEmail()));
    }
}
