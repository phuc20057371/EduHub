package com.example.eduhubvn.controller;

import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.auth.*;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.services.AuthenticationService;
import com.example.eduhubvn.services.EmailService;
import com.example.eduhubvn.services.OtpService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final OtpService otpService;
    private final EmailService emailService;

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
            HttpServletRequest request) {
        try {
            AuthenResponse response = authenticationService.refreshToken(request);
            if (response == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Refresh token không hợp lệ hoặc hết hạn", null));
            }
            return ResponseEntity.ok(ApiResponse.success("Làm mới token thành công", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Refresh token không hợp lệ hoặc hết hạn", null));
        }
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody Email email) throws MessagingException {
        return ResponseEntity.ok(otpService.sendEmail(email.getEmail()));
    }

    @PostMapping("/send-mail")
    public String sendEmail(@RequestBody EmailSent email) throws MessagingException {
        String to = email.getTo();
        if (otpService.isValidEmail(to)) {
            return "Invalid email address";
        }
        emailService.sendHtmlEmail(to, email.getSubject(), email.getBody());
        return "Email sent successfully";
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            String message = authenticationService.forgotPassword(request);
            return ResponseEntity.ok(ApiResponse.success(message, null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Có lỗi xảy ra khi gửi OTP", null));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            String message = authenticationService.resetPassword(request);
            return ResponseEntity.ok(ApiResponse.success(message, null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Có lỗi xảy ra khi đặt lại mật khẩu", null));
        }
    }

    ///
    
}
