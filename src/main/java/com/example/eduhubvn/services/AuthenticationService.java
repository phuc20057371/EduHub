package com.example.eduhubvn.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.eduhubvn.dtos.auth.Email;
import com.example.eduhubvn.dtos.auth.EmailOtp;
import com.example.eduhubvn.dtos.auth.request.ChangePasswordReq;
import com.example.eduhubvn.dtos.auth.request.LoginReq;
import com.example.eduhubvn.dtos.auth.request.RegisterReq;
import com.example.eduhubvn.dtos.auth.request.ResetPasswordReq;
import com.example.eduhubvn.dtos.auth.response.AuthenResponse;
import com.example.eduhubvn.entities.Role;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;

    public AuthenResponse register(RegisterReq request) {
        if (request.getRole() == null) {
            request.setRole(Role.USER);
        }

        if (userRepository.existsByEmail((request.getEmail()))) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .lastLogin(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        String jwtToken = jwtService.generateToken(savedUser);
        String refreshToken = jwtService.generateRefreshToken(savedUser);

        return AuthenResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenResponse login(LoginReq request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Người dùng không tồn tại"));

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthenResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenResponse refreshToken(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid Authorization header");
        }

        final String refreshToken = authHeader.substring(7);

        final String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail == null) {
            return null;
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!jwtService.isTokenValid(refreshToken)) {
            return null;
        }

        String newAccessToken = jwtService.generateToken(user);

        return AuthenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String forgotPassword(Email request) throws Exception {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống");
        }

        String email = request.getEmail().trim();

        // Kiểm tra email có tồn tại trong hệ thống không (bao gồm subEmails)
        Optional<User> userOpt = userRepository.findByEmailOrSubEmail(email);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Email không tồn tại trong hệ thống");
        }

        // Gửi OTP qua email
        return otpService.sendForgotPasswordOtp(email);
    }

    public String resetPassword(ResetPasswordReq request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống");
        }
        if (request.getOtp() == null || request.getOtp().trim().isEmpty()) {
            throw new IllegalArgumentException("OTP không được để trống");
        }
        if (request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu mới không được để trống");
        }
        if (request.getNewPassword().length() < 6) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự");
        }

        String email = request.getEmail().trim();
        String otp = request.getOtp().trim();
        String newPassword = request.getNewPassword().trim();

        // Tìm user bằng email chính hoặc subEmails
        Optional<User> userOpt = userRepository.findByEmailOrSubEmail(email);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Email không tồn tại trong hệ thống");
        }

        User user = userOpt.get();

        // Xác thực OTP với email chính của user
        if (!otpService.validate(user.getEmail(), otp)) {
            throw new IllegalArgumentException("OTP không hợp lệ hoặc đã hết hạn");
        }

        // Cập nhật mật khẩu
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "Mật khẩu đã được đặt lại thành công";
    }

    public String sendOTPChangePassword(Email request, User user) throws Exception {
        System.out.println("DEBUG: Starting sendOTPChangePassword method");

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống");
        }

        String email = request.getEmail().trim().toLowerCase();
        System.out.println("DEBUG: Processing OTP request for email: " + email);
        System.out.println("DEBUG: User ID: " + (user != null ? user.getId() : "null"));
        System.out.println("DEBUG: User main email: " + (user != null ? user.getEmail() : "null"));

        // Kiểm tra null safety cho subEmails
        if (user != null && user.getSubEmails() != null) {
            System.out.println("DEBUG: User sub emails: " + user.getSubEmails());
        } else {
            System.out.println("DEBUG: User sub emails: null or user is null");
        }

        // Kiểm tra định dạng email
        if (!isValidEmailFormat(email)) {
            System.out.println("DEBUG: Invalid email format: " + email);
            throw new IllegalArgumentException("Định dạng email không hợp lệ");
        }

        // Kiểm tra email có thuộc về user không
        boolean isMainEmail = user.getEmail().equalsIgnoreCase(email);
        boolean isSubEmail = user.getSubEmails() != null &&
                user.getSubEmails().stream()
                        .anyMatch(subEmail -> subEmail.equalsIgnoreCase(email));

        System.out.println("DEBUG: Is main email: " + isMainEmail);
        System.out.println("DEBUG: Is sub email: " + isSubEmail);

        if (!isMainEmail && !isSubEmail) {
            System.out.println("DEBUG: Email does not belong to user");
            throw new IllegalArgumentException("Email này không thuộc về tài khoản của bạn");
        }

        try {
            // Gửi OTP qua email
            System.out.println("DEBUG: Attempting to send OTP to: " + email);
            String result = otpService.sendChangePasswordOtp(email);
            System.out.println("DEBUG: OTP service result: " + result);

            // Thông báo loại email để user biết
            String emailType = isMainEmail ? "email chính" : "email phụ";
            return String.format("OTP đã được gửi đến %s (%s) của bạn", emailType, email);
        } catch (Exception e) {
            System.err.println("DEBUG: Error sending OTP: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public String changePassword(ChangePasswordReq request, User user) {
        // Validation cơ bản
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống");
        }
        if (request.getOtp() == null || request.getOtp().trim().isEmpty()) {
            throw new IllegalArgumentException("OTP không được để trống");
        }
        if (request.getOldPassword() == null || request.getOldPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu cũ không được để trống");
        }
        if (request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu mới không được để trống");
        }
        if (request.getConfirmPassword() == null || request.getConfirmPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Xác nhận mật khẩu không được để trống");
        }

        String email = request.getEmail().trim().toLowerCase();
        String otp = request.getOtp().trim();
        String oldPassword = request.getOldPassword().trim();
        String newPassword = request.getNewPassword().trim();
        String confirmPassword = request.getConfirmPassword().trim();

        System.out.println("DEBUG: Change password request for email: " + email);
        System.out.println("DEBUG: User ID: " + (user != null ? user.getId() : "null"));

        // Kiểm tra mật khẩu cũ có đúng không
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu cũ không chính xác");
        }

        // Kiểm tra mật khẩu mới và xác nhận khớp nhau
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Mật khẩu mới và xác nhận mật khẩu không khớp");
        }

        // Kiểm tra mật khẩu mới không được giống mật khẩu cũ
        if (oldPassword.equals(newPassword)) {
            throw new IllegalArgumentException("Mật khẩu mới phải khác mật khẩu cũ");
        }

        // Kiểm tra độ dài mật khẩu
        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự");
        }

        // Kiểm tra định dạng email
        if (!isValidEmailFormat(email)) {
            throw new IllegalArgumentException("Định dạng email không hợp lệ");
        }

        // Kiểm tra email có thuộc về user không
        boolean isMainEmail = user.getEmail().equalsIgnoreCase(email);
        boolean isSubEmail = user.getSubEmails() != null &&
                user.getSubEmails().stream()
                        .anyMatch(subEmail -> subEmail.equalsIgnoreCase(email));

        if (!isMainEmail && !isSubEmail) {
            throw new IllegalArgumentException("Email này không thuộc về tài khoản của bạn");
        }

        // Validate OTP với email được yêu cầu
        if (!otpService.validate(email, otp)) {
            throw new IllegalArgumentException("OTP không hợp lệ hoặc đã hết hạn");
        }

        try {
            // Cập nhật mật khẩu
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            System.out.println("DEBUG: Password changed successfully for user: " + user.getId());

            // Xóa OTP đã sử dụng (tùy chọn - có thể implement trong OtpService)
            // otpService.removeOtp(email);

            String emailType = isMainEmail ? "email chính" : "email phụ";
            return String.format("Mật khẩu đã được thay đổi thành công cho %s (%s)", emailType, email);

        } catch (Exception e) {
            System.err.println("DEBUG: Error changing password: " + e.getMessage());
            throw new RuntimeException("Có lỗi xảy ra khi thay đổi mật khẩu", e);
        }
    }

    public String sendOTPAddSubEmail(Email request, User user) throws Exception {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống");
        }

        String email = request.getEmail().trim().toLowerCase();
        System.out.println("DEBUG: Send OTP for add sub-email: " + email);
        System.out.println("DEBUG: User ID: " + (user != null ? user.getId() : "null"));

        // Kiểm tra định dạng email
        if (!isValidEmailFormat(email)) {
            throw new IllegalArgumentException("Định dạng email không hợp lệ");
        }

        // Kiểm tra email không được trùng với email chính
        if (user.getEmail().equalsIgnoreCase(email)) {
            throw new IllegalArgumentException("Email này đã là email chính của tài khoản");
        }

        // Kiểm tra email chưa tồn tại trong sub-emails
        if (user.getSubEmails() != null &&
                user.getSubEmails().stream().anyMatch(subEmail -> subEmail.equalsIgnoreCase(email))) {
            throw new IllegalArgumentException("Email này đã được thêm vào tài khoản");
        }

        // Kiểm tra email chưa được sử dụng bởi user khác
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email này đã được sử dụng bởi tài khoản khác");
        }

        // Kiểm tra email có trong sub-emails của user khác không
        Optional<User> existingUser = userRepository.findBySubEmail(email);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email này đã được sử dụng bởi tài khoản khác");
        }

        try {
            // Gửi OTP qua email
            otpService.sendChangePasswordOtp(email);
            return String.format("OTP đã được gửi đến email %s để xác thực", email);
        } catch (Exception e) {
            System.err.println("DEBUG: Error sending OTP for add sub-email: " + e.getMessage());
            throw e;
        }
    }

    public String addSubEmail(EmailOtp request, User user) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống");
        }
        if (request.getOtp() == null || request.getOtp().trim().isEmpty()) {
            throw new IllegalArgumentException("OTP không được để trống");
        }

        String email = request.getEmail().trim().toLowerCase();
        String otp = request.getOtp().trim();

        System.out.println("DEBUG: Add sub-email request: " + email);
        System.out.println("DEBUG: User ID: " + (user != null ? user.getId() : "null"));

        // Kiểm tra định dạng email
        if (!isValidEmailFormat(email)) {
            throw new IllegalArgumentException("Định dạng email không hợp lệ");
        }

        // Kiểm tra email không được trùng với email chính
        if (user.getEmail().equalsIgnoreCase(email)) {
            throw new IllegalArgumentException("Email này đã là email chính của tài khoản");
        }

        // Kiểm tra email chưa tồn tại trong sub-emails
        if (user.getSubEmails() != null &&
                user.getSubEmails().stream().anyMatch(subEmail -> subEmail.equalsIgnoreCase(email))) {
            throw new IllegalArgumentException("Email này đã được thêm vào tài khoản");
        }

        // Validate OTP
        if (!otpService.validate(email, otp)) {
            throw new IllegalArgumentException("OTP không hợp lệ hoặc đã hết hạn");
        }

        // Kiểm tra lại email chưa được sử dụng (double check)
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email này đã được sử dụng bởi tài khoản khác");
        }

        Optional<User> existingUser = userRepository.findBySubEmail(email);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email này đã được sử dụng bởi tài khoản khác");
        }

        try {
            // Thêm sub-email vào user
            user.getSubEmails().add(email);
            userRepository.save(user);

            System.out.println("DEBUG: Sub-email added successfully: " + email);
            return String.format("Email %s đã được thêm vào tài khoản thành công", email);

        } catch (Exception e) {
            System.err.println("DEBUG: Error adding sub-email: " + e.getMessage());
            throw new RuntimeException("Có lỗi xảy ra khi thêm email", e);
        }
    }

    public String removeSubEmail(Email request, User user) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống");
        }

        String email = request.getEmail().trim().toLowerCase();
        System.out.println("DEBUG: Remove sub-email request: " + email);
        System.out.println("DEBUG: User ID: " + (user != null ? user.getId() : "null"));

        // Kiểm tra định dạng email
        if (!isValidEmailFormat(email)) {
            throw new IllegalArgumentException("Định dạng email không hợp lệ");
        }

        // Kiểm tra email có tồn tại trong sub-emails không
        if (user.getSubEmails() == null || user.getSubEmails().isEmpty()) {
            throw new IllegalArgumentException("Tài khoản chưa có sub-email nào");
        }

        boolean emailExists = user.getSubEmails().stream()
                .anyMatch(subEmail -> subEmail.equalsIgnoreCase(email));

        if (!emailExists) {
            throw new IllegalArgumentException("Email này không tồn tại trong danh sách sub-email của bạn");
        }

        try {
            // Xóa sub-email khỏi user
            user.getSubEmails().removeIf(subEmail -> subEmail.equalsIgnoreCase(email));
            userRepository.save(user);

            System.out.println("DEBUG: Sub-email removed successfully: " + email);
            return String.format("Email %s đã được xóa khỏi tài khoản thành công", email);

        } catch (Exception e) {
            System.err.println("DEBUG: Error removing sub-email: " + e.getMessage());
            throw new RuntimeException("Có lỗi xảy ra khi xóa email", e);
        }
    }

    public Set<String> getSubEmails(User user) {
        System.out.println("DEBUG: Get sub-emails for user: " + (user != null ? user.getId() : "null"));
        return user.getSubEmails();
    }

    private boolean isValidEmailFormat(String email) {
        return email != null &&
                email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

}
