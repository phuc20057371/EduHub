package com.example.eduhubvn.config;

import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.example.eduhubvn.services.JwtService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // Chỉ kiểm tra token khi CONNECT
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
            
            if (token == null || token.isEmpty()) {
                System.err.println("WebSocket connection denied: No token provided");
                throw new RuntimeException("Authentication required");
            }

            // Loại bỏ "Bearer " prefix nếu có
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            try {
                // Validate token
                if (!jwtService.isTokenValid(token)) {
                    System.err.println("WebSocket connection denied: Invalid token");
                    throw new RuntimeException("Invalid token");
                }
                
                // Lấy username từ token và set vào session
                String username = jwtService.extractUsername(token);
                accessor.setUser(() -> username);
                
                System.out.println("WebSocket connection accepted for user: " + username);
                
            } catch (Exception e) {
                System.err.println("WebSocket connection denied: " + e.getMessage());
                throw new RuntimeException("Authentication failed");
            }
        }

        return message;
    }

}
