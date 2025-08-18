package com.example.eduhubvn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "application.security.jwt")
public class JwtProperties {
    private long expiredAccess;
    private long expiredRefresh;

}
