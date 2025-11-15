package com.example.eduhubvn.config;

import com.example.eduhubvn.dtos.auth.response.AuthenResponse;
import com.example.eduhubvn.services.Oauth2Service;
import io.swagger.v3.core.util.Json;
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
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static com.example.eduhubvn.entities.Permission.*;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;
    private final Oauth2Service oauth2Service;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> {
                    httpSecurityCorsConfigurer.configurationSource(request -> {
                        var cors = new CorsConfiguration();
                        cors.setAllowedOriginPatterns(List.of("*")); // Cho phép tất cả origins
                        cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                        cors.setAllowedHeaders(List.of("*"));
                        cors.setAllowCredentials(true); // Vẫn giữ credentials
                        return cors;
                    });
                })
                .authorizeHttpRequests(auth -> auth
                        // ✅ Public endpoints
                        .requestMatchers(
                                "/login/**",
                                "/oauth2/authorization/**",
                                "/oauth2/**",
                                "/api/v1/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/ws/**",
                                "/uploads/**",
                                "/public/**"
                        ).permitAll()

                        // ✅ Organization permissions
                        .requestMatchers(HttpMethod.GET, "/api/v1/organization/**")
                        .hasAuthority(ORGANIZATION_READ.getPermission())
                        .requestMatchers(HttpMethod.POST, "/api/v1/organization/**")
                        .hasAuthority(ORGANIZATION_CREATE.getPermission())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/organization/**")
                        .hasAuthority(ORGANIZATION_UPDATE.getPermission())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/organization/**")
                        .hasAuthority(ORGANIZATION_DELETE.getPermission())

                        // ✅ Lecturer permissions
                        .requestMatchers(HttpMethod.GET, "/api/v1/lecturer/**")
                        .hasAuthority(LECTURER_READ.getPermission())
                        .requestMatchers(HttpMethod.POST, "/api/v1/lecturer/**")
                        .hasAuthority(LECTURER_CREATE.getPermission())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/lecturer/**")
                        .hasAuthority(LECTURER_UPDATE.getPermission())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/lecturer/**")
                        .hasAuthority(LECTURER_DELETE.getPermission())
                        .anyRequest().authenticated()
                )
                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> {
                    httpSecurityOAuth2LoginConfigurer
                            .successHandler((request, response, authentication) -> {
                                OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
                                OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
                                AuthenResponse authResponse = oauth2Service.authenticate(oAuth2User);
                                String script = "<script>" +
                                        "window.opener.postMessage({authResponse: " + Json.pretty(authResponse) +
//                                        "}, 'http://localhost:3000');" +
                                        "}, 'http://"+ request.getServerName() +"/');" +
                                        "window.close();" +
                                        "</script>";
                                response.setContentType("text/html");
                                response.getWriter().write(script);
                            })
                            .failureHandler((request, response, exception) -> {
//                                response.sendRedirect("http://localhost:3000");
                                response.sendRedirect("http://" + request.getServerName() + "/");
                            });
                })
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}