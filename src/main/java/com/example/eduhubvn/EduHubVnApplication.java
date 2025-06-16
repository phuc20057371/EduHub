package com.example.eduhubvn;

import com.example.eduhubvn.dtos.RegisterRequest;
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
                .name("user")
                .email("user")
                .password("test")
                .role(Role.USER)
                .build();
        System.out.println("token user: " + authenticationService.register(user).getAccessToken());
        var lecturer = RegisterRequest.builder()
                .name("lecturer")
                .email("lecturer")
                .password("test")
                .role(Role.LECTURER)
                .build();
        System.out.println("token lecturer: " + authenticationService.register(lecturer).getAccessToken());
        var organization = RegisterRequest.builder()
                .name("organization")
                .email("organization")
                .password("test")
                .role(Role.ORGANIZATION)
                .build();
        System.out.println("token organization: " + authenticationService.register(organization).getAccessToken());
    };


}

}
