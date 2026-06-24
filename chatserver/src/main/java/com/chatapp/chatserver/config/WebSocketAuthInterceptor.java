package com.chatapp.chatserver.config;

import com.chatapp.chatserver.service.JwtService;
import com.chatapp.chatserver.service.UserService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor{
    private final JwtService jwtService;
    private final UserService userService;

    public WebSocketAuthInterceptor(JwtService jwtService, UserService userService){
        this.jwtService = jwtService;
        this.userService = userService;
    }


    // Instiated at the connect frame and starts authorization
    @Override
    public Message<?> presend(Message<?> message, MessageChannel MessageChannel){
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())){

            String authHeader= accessor.getFirstNativeHeader("Authorization");

            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                return message;
            }

            String token = authHeader.substring(7);

            if (!jwtService.isTokenValid(token)){
                return message;
            }

            String username = jwtService.extractUsername(token);

            UserDetails userDetails = userService.loadUserByUsername(username);

            // If all passes through then we want to build our authentication
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,
                                                                                                null,
                                                                                                userDetails.getAuthorities());

            accessor.setUser(auth);

        }
        return message;
    }
}