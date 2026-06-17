package com.chatapp.chatserver.controller;

import com.chatapp.chatserver.dto.AuthResponse;
import com.chatapp.chatserver.dto.LoginRequest;
import com.chatapp.chatserver.dto.RegisterRequest;
import com.chatapp.chatserver.service.JwtService;
import com.chatapp.chatserver.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService,JwtService jwtService,AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    //Registering each user with a token
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        userService.register(request.username(), request.password());
        String token = jwtService.generateToken(request.username());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // This line throws if credentials are bad
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(), request.password()));

        //All credentials are valid make a token and give it to the user
        String token = jwtService.generateToken(request.username());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}