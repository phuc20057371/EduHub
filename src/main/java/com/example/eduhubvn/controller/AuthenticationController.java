package com.example.eduhubvn.controller;

import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.auth.AuthenResponse;
import com.example.eduhubvn.dtos.auth.Email;
import com.example.eduhubvn.dtos.auth.LoginRequest;
import com.example.eduhubvn.dtos.auth.RegisterRequest;
import com.example.eduhubvn.services.AuthenticationService;
import com.example.eduhubvn.services.OtpService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse<AuthenResponse>> register(@RequestBody RegisterRequest request) {
        if (!otpService.validate(request.getEmail(), request.getOtp())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("OTP không hợp lệ", null));
        }

        AuthenResponse response = authenticationService.register(request);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Đăng ký thất bại", null));
        }

        return ResponseEntity.ok(ApiResponse.success("Đăng ký thành công", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenResponse>> login(@RequestBody LoginRequest request) {
        AuthenResponse response = authenticationService.login(request);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Tài khoản hoặc mật khẩu không chính xác", null));
        }
        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthenResponse>> refreshToken(
            HttpServletRequest request
    ) {
        AuthenResponse response = authenticationService.refreshToken(request);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Refresh token không hợp lệ hoặc hết hạn", null));
        }
        return ResponseEntity.ok(ApiResponse.success("Làm mới token thành công", response));
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(
            @RequestBody Email email
    ) throws MessagingException {
        return ResponseEntity.ok(otpService.sendEmail(email.getEmail()));
    }
}
