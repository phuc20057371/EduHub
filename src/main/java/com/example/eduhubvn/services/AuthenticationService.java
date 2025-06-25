package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.auth.AuthenResponse;
import com.example.eduhubvn.dtos.auth.LoginRequest;
import com.example.eduhubvn.dtos.auth.RegisterRequest;
import com.example.eduhubvn.entities.Role;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenResponse register(RegisterRequest request) {
        if (request.getRole()==null){
            request.setRole(Role.USER);
        }
        try {
            var user = User.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .phone(request.getPhone())
                    .role(request.getRole())
                    .lastLogin(LocalDateTime.now())
                    .build();
            var savedUser = userRepository.save(user);
            var jwtToken = jwtService.generateToken(savedUser);
            var refreshToken = jwtService.generateRefreshToken(savedUser);

            return AuthenResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        }catch (Exception e){
            return null;
        }
    }

    public AuthenResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        try {
            User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            return AuthenResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (Exception e) {

            return null;
        }

    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if(userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail).orElseThrow();
            if (jwtService.isTokenValid(refreshToken)) {
              var accessToken = jwtService.generateToken(user);
              var authenResponse = AuthenResponse.builder()
                      .accessToken(accessToken)
                      .refreshToken(refreshToken)
                      .build();
              new ObjectMapper().writeValue(response.getOutputStream(), authenResponse);
            }
        }
    }
}
