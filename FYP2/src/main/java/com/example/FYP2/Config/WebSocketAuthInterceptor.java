package com.example.FYP2.Config;

import com.example.FYP2.Service.JWTUtils;
import com.example.FYP2.Service.UserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * WebSocket authentication interceptor.
 * Validates JWT token on STOMP CONNECT to prevent unauthorized WebSocket access.
 * Clients must send the JWT as a native header: Authorization: Bearer <token>
 */
@Slf4j
@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserDetailService userDetailService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("WebSocket CONNECT rejected: no Bearer token provided");
                throw new IllegalArgumentException("Missing or invalid Authorization header");
            }

            String jwtToken = authHeader.substring(7);

            try {
                String userEmail = jwtUtils.extractUsername(jwtToken);

                if (userEmail != null) {
                    UserDetails userDetails = userDetailService.loadUserByUsername(userEmail);

                    if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities()
                                );
                        accessor.setUser(authentication);
                        log.debug("WebSocket CONNECT authenticated for user: {}", userEmail);
                    } else {
                        log.warn("WebSocket CONNECT rejected: invalid token for user: {}", userEmail);
                        throw new IllegalArgumentException("Invalid JWT token");
                    }
                }
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception e) {
                log.warn("WebSocket CONNECT rejected: token parsing failed", e);
                throw new IllegalArgumentException("Invalid JWT token");
            }
        }

        return message;
    }
}
