package com.chatapp.chatserver.config;

import com.chatapp.chatserver.service.JwtService;
import com.chatapp.chatserver.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtService jwtService, UserService userService){
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                    @NonNull HttpServletResponse response, 
                                    @NonNull FilterChain filterChain) throws ServletException, IOException
    {
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer")){
            filterChain.doFilter(request, response);
            return;
        }

        //Get raw token
        String token = authHeader.substring(7);


        // validate the token
        if (jwtService.isTokenValid(token) == false){
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtService.extractUsername(token);

        UserDetails userDetails = userService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authToken = 
                                                        new UsernamePasswordAuthenticationToken(
                                                            userDetails, null, userDeatils.getAuthorities()
                                                        );
        authToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request,response);
    }
    
}