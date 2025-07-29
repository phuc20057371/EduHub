package com.example.eduhubvn;

import com.example.eduhubvn.dtos.auth.RegisterRequest;
import com.example.eduhubvn.entities.Role;
import com.example.eduhubvn.repositories.UserRepository;
import com.example.eduhubvn.services.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EduHubVnApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduHubVnApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(AuthenticationService authenticationService, UserRepository userRepository) {
        return args -> {

            try {
                if (userRepository.findByEmail("admin@gmail.com").isPresent()) {
                    return;
                }
                // Create 10 LECTURER accounts
                for (int i = 1; i <= 100; i++) {
                    var lecturer = RegisterRequest.builder()
                            .email("lecturer" + i + "@gmail.com")
                            .password("SGL@2025")
                            .role(Role.LECTURER)
                            .build();
                    System.out.println("token lecturer" + i + ": " + authenticationService.register(lecturer).getAccessToken());
                }
                // Create 10 SCHOOL accounts
                for (int i = 1; i <= 10; i++) {
                    var school = RegisterRequest.builder()
                            .email("school" + i + "@gmail.com")
                            .password("SGL@2025")
                            .role(Role.SCHOOL)
                            .build();
                    System.out.println("token school" + i + ": " + authenticationService.register(school).getAccessToken());
                }
                // Create 10 ORGANIZATION accounts
                for (int i = 1; i <= 10; i++) {
                    var organization = RegisterRequest.builder()
                            .email("organization" + i + "@gmail.com")
                            .password("SGL@2025")
                            .role(Role.ORGANIZATION)
                            .build();
                    System.out.println("token organization" + i + ": " + authenticationService.register(organization).getAccessToken());
                }
                // Create 20 USER accounts
                for (int i = 1; i <= 10; i++) {
                    var user = RegisterRequest.builder()
                            .email("user" + i + "@gmail.com")
                            .password("SGL@2025")
                            .role(Role.USER)
                            .build();
                    System.out.println("token user" + i + ": " + authenticationService.register(user).getAccessToken());
                }
                // Create 1 ADMIN user
                var admin = RegisterRequest.builder()
                        .email("admin@gmail.com")
                        .password("SGL@2025")
                        .role(Role.ADMIN)
                        .build();
                System.out.println("token admin: " + authenticationService.register(admin).getAccessToken());
            } catch (Exception e) {
                throw new RuntimeException("không thể khởi tạo dữ liệu mẫu: " + e.getMessage(), e);
            }
        };
    }
}


