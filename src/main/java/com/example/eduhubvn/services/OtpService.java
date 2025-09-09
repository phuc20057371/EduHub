package com.example.eduhubvn.services;

import com.example.eduhubvn.entities.Otp;
import com.example.eduhubvn.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
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
        return !pattern.matcher(email).matches();
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
    public String sendEmail(String email) throws MessagingException {
        email = email.trim();
        if (isValidEmail(email)) { // sửa điều kiện đúng logic
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

    public Otp generateOtp() {
        // Lấy giờ hiện tại
        LocalDateTime now = LocalDateTime.now();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return new Otp(otp.toString(), now.plusMinutes(2));
    }
}
