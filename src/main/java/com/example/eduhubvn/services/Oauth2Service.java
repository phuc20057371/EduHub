package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.auth.AuthenResponse;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.eduhubvn.entities.Role.USER;

@Service
@RequiredArgsConstructor
public class Oauth2Service {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    public AuthenResponse authenticate(OAuth2User oAuth2User) {
        // kiểm tra xem user đã tồn tại trong db chưa nếu chưa thì thêm vào
        Optional<User> currentUser = userRepository.findByEmail(oAuth2User.getAttribute("email"));

        AuthenResponse authenticationResponse;
        if (currentUser.isEmpty()) {
            User user = User.builder()
                    .email(oAuth2User.getAttribute("email"))
                    .lastLogin(LocalDateTime.now())
                    .role(USER)
                    .build();
            userRepository.save(user);

            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            authenticationResponse = AuthenResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            var jwtToken = jwtService.generateToken(currentUser.get());
            var refreshToken = jwtService.generateRefreshToken(currentUser.get());
            authenticationResponse = AuthenResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        return authenticationResponse;
    }
}

