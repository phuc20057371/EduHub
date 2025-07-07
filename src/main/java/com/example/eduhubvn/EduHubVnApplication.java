package com.example.eduhubvn;

import com.example.eduhubvn.dtos.auth.RegisterRequest;
import com.example.eduhubvn.entities.Role;
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
public CommandLineRunner init(AuthenticationService authenticationService) {
    return args -> {
        var user =  RegisterRequest.builder()
                .email("user@gmail.com")
                .password("123")
                .role(Role.USER)
                .build();
        System.out.println("token user: " + authenticationService.register(user).getAccessToken());
        var user1 =  RegisterRequest.builder()
                .email("user1@gmail.com")
                .password("123")
                .role(Role.USER)
                .build();
        System.out.println("token user: " + authenticationService.register(user1).getAccessToken());
        var user2 =  RegisterRequest.builder()
                .email("user2@gmail.com")
                .password("123")
                .role(Role.USER)
                .build();
        System.out.println("token user: " + authenticationService.register(user2).getAccessToken());
        var lecturer = RegisterRequest.builder()
                .email("lecturer@gmail.com")
                .password("123")
                .role(Role.USER)
                .build();
        System.out.println("token lecturer: " + authenticationService.register(lecturer).getAccessToken());
        var organization = RegisterRequest.builder()
                .email("organization@gmail.com")
                .password("123")
                .role(Role.USER)
                .build();
        System.out.println("token organization: " + authenticationService.register(organization).getAccessToken());
        var admin = RegisterRequest.builder()
                .email("admin@gmail.com")
                .password("123")
                .role(Role.ADMIN)
                .build();
        System.out.println("token admin: " + authenticationService.register(admin).getAccessToken());
    };


}

}
