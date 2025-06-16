package com.example.eduhubvn.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.eduhubvn.entities.Permission.*;
import static com.example.eduhubvn.entities.Role.*;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "api/v1/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/ws/**" )
                        .permitAll()

                        .requestMatchers(
                                "/api/v1/organization/**"
                        ).hasAnyRole(ORGANIZATION.name())

                        .requestMatchers(HttpMethod.GET, "/api/v1/organization/**").hasAnyAuthority(ORGANIZATION_READ.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/organization/**").hasAnyAuthority(ORGANIZATION_CREATE.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/organization/**").hasAnyAuthority(ORGANIZATION_UPDATE.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/organization/**").hasAnyAuthority(ORGANIZATION_DELETE.name())

                       .requestMatchers(
                                "/api/v1/lecturer/**"
                        ).hasRole(LECTURER.name())

                        .requestMatchers(HttpMethod.GET, "/api/v1/lecturer/**").hasAuthority(LECTURER_READ.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/lecturer/**").hasAuthority(LECTURER_CREATE.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/lecturer/**").hasAuthority(LECTURER_UPDATE.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/lecturer/**").hasAuthority(LECTURER_DELETE.name())

                        .anyRequest()
                        .authenticated()
                ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}