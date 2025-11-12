package com.example.eduhubvn.services;

import com.example.eduhubvn.entities.Otp;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final Map<String, Otp> otpMap = new HashMap<>();
    private final JavaMailSender javaMailSender;
    private static final String CHARACTERS = "0123456789";
    private static final int OTP_LENGTH = 6;
    private final SecureRandom random = new SecureRandom();

    private final UserRepository userRepository;

    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches(); // Trả về true nếu email HỢP LỆ
    }
    // public String sendEmail(String email) throws MessagingException {
    // email = email.trim();
    // if (isValidEmail(email)) {
    // return "Invalid email";
    // }
    // if (userRepository.existsByEmail(email)) {
    // throw new MessagingException("Email already exists");
    // }

    // try {
    // SimpleMailMessage message = new SimpleMailMessage();
    // message.setTo(email);
    // message.setSubject("subject");
    // Otp otp = generateOtp();
    // message.setText(otp.getOtp());
    // otpMap.put(email, otp);
    // javaMailSender.send(message);
    // System.out.println("Mail sent successfully...");
    // return "Mail sent successfully...";
    // } catch (MailException e) {
    // System.err.println("Error sending email: " + e.getMessage());
    // throw new MessagingException("Failed to send email", e);
    // }
    // }
    public String sendEmail(String email) throws MessagingException, UnsupportedEncodingException {
        email = email.trim();
        if (!isValidEmail(email)) { // Kiểm tra email không hợp lệ
            return "Invalid email";
        }
        if (userRepository.existsByEmail(email)) {
            throw new MessagingException("Email already exists");
        }

        try {
            // Tạo OTP
            Otp otp = generateOtp();
            otpMap.put(email, otp);

            // Tạo MimeMessage (hỗ trợ HTML)
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("eduhubvn.otp@ccvi.com.vn", "EduhubVN OTP Service");
            helper.setTo(email);
            helper.setSubject("Your OTP Verification Code");

            String htmlContent = "<!DOCTYPE html>"
                    + "<html>"
                    + "<body style='font-family: Arial, sans-serif; color: #333;'>"
                    + "<h2 style='color:#4CAF50;'>Xin chào!</h2>"
                    + "<p>Bạn đã yêu cầu một mã OTP để xác thực email của mình.</p>"
                    + "<p style='font-size:16px;'>Mã OTP của bạn là:</p>"
                    + "<h1 style='color:#4CAF50;'>" + otp.getOtp() + "</h1>"
                    + "<br>"
                    + "<p>Trân trọng,</p>"
                    + "<p><b>EduhubVN Team</b></p>"
                    + "</body>"
                    + "</html>";

            helper.setText(htmlContent, true); // true => gửi HTML

            javaMailSender.send(mimeMessage);

            System.out.println("Mail sent successfully...");
            return "Mail sent successfully...";
        } catch (MailException e) {
            System.err.println("Error sending email: " + e.getMessage());
            throw new MessagingException("Failed to send email", e);
        }
    }

    public Boolean validate(String email, String otp) {
        if (otpMap.containsKey(email) && otpMap.get(email).getOtp().equals(otp)
                && otpMap.get(email).getExpiryDate().isAfter(LocalDateTime.now())) {
            otpMap.remove(email);
            return true;
        }
        return false;
    }

    public String sendForgotPasswordOtp(String email) throws MessagingException {
        email = email.trim();
        if (!isValidEmail(email)) {
            return "Invalid email";
        }

        // Tìm user bằng email chính hoặc subEmails
        Optional<User> userOpt = userRepository.findByEmailOrSubEmail(email);
        if (userOpt.isEmpty()) {
            throw new MessagingException("Email không tồn tại trong hệ thống");
        }

        User user = userOpt.get();

        try {
            // Tạo OTP và lưu với email chính của user (để validate sau này)
            Otp otp = generateOtp();
            otpMap.put(user.getEmail(), otp);

            // Tạo MimeMessage (hỗ trợ HTML)
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(email); // Gửi đến email được yêu cầu (có thể là subEmail)
            helper.setSubject("Mã OTP - EduHubVN");

            String htmlContent = "<!DOCTYPE html>"
                    + "<html>"
                    + "<body style='font-family: Arial, sans-serif; color: #333;'>"
                    + "<h2 style='color:#4CAF50;'>Đặt lại mật khẩu</h2>"
                    + "<p>Bạn đã yêu cầu đặt 1 mã OTP cho tài khoản của mình.</p>"
                    + "<p style='font-size:16px;'>Mã OTP của bạn là:</p>"
                    + "<h1 style='color:#4CAF50;'>" + otp.getOtp() + "</h1>"
                    + "<p style='color:#666;'>Mã này sẽ hết hạn sau 5 phút.</p>"
                    + "<br>"
                    + "<p>Nếu bạn không yêu cầu , vui lòng bỏ qua email này.</p>"
                    + "<p>Trân trọng,</p>"
                    + "<p><b>EduHubVN Team</b></p>"
                    + "</body>"
                    + "</html>";

            helper.setText(htmlContent, true); // true => gửi HTML

            javaMailSender.send(mimeMessage);

            System.out.println("Forgot password OTP sent successfully...");
            return "OTP đã được gửi đến email của bạn";
        } catch (MailException e) {
            System.err.println("Error sending forgot password OTP: " + e.getMessage());
            throw new MessagingException("Failed to send forgot password OTP", e);
        }
    }

    public String sendChangePasswordOtp(String email) throws MessagingException {
        email = email.trim();
        if (!isValidEmail(email)) {
            return "Invalid email";
        }

        try {
            // Tạo OTP và lưu với email chính của user (để validate sau này)
            Otp otp = generateOtp();
            otpMap.put(email, otp);

            // Tạo MimeMessage (hỗ trợ HTML)
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(email); // Gửi đến email được yêu cầu (có thể là subEmail)
            helper.setSubject("Mã OTP - EduHubVN");

            String htmlContent = "<!DOCTYPE html>"
                    + "<html>"
                    + "<body style='font-family: Arial, sans-serif; color: #333;'>"
                    + "<h2 style='color:#4CAF50;'>Đặt lại mật khẩu</h2>"
                    + "<p>Bạn đã yêu cầu 1 mã OTP cho tài khoản của mình.</p>"
                    + "<p style='font-size:16px;'>Mã OTP của bạn là:</p>"
                    + "<h1 style='color:#4CAF50;'>" + otp.getOtp() + "</h1>"
                    + "<p style='color:#666;'>Mã này sẽ hết hạn sau 5 phút.</p>"
                    + "<br>"
                    + "<p>Nếu bạn không yêu cầu, vui lòng bỏ qua email này.</p>"
                    + "<p>Trân trọng,</p>"
                    + "<p><b>EduHubVN Team</b></p>"
                    + "</body>"
                    + "</html>";

            helper.setText(htmlContent, true); // true => gửi HTML

            javaMailSender.send(mimeMessage);

            System.out.println("Forgot password OTP sent successfully...");
            return "OTP đã được gửi đến email của bạn";
        } catch (MailException e) {
            System.err.println("Error sending forgot password OTP: " + e.getMessage());
            throw new MessagingException("Failed to send forgot password OTP", e);
        }

    }

    public Otp generateOtp() {
        // Lấy giờ hiện tại
        LocalDateTime now = LocalDateTime.now();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return new Otp(otp.toString(), now.plusMinutes(5)); // Tăng thời gian hết hạn lên 5 phút
    }

}
