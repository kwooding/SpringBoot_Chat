package com.chatapp.chatserver.service;

import com.chatapp.chatserver.model.UserEntity;
import com.chatapp.chatserver.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.List;

@Service
public class UserService implements UserDetailsService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity register(String username, String rawPassword){
        
        if(userRepository.findByUsername(username).isPresent()){
            throw new IllegalArgumentException("Username " + username + " already exists");
        }
        

        String hashed = passwordEncoder.encode(rawPassword);

        UserEntity user = new UserEntity(username, hashed, "ROLE_USER", Instant.now());

        return userRepository.save(user);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Check for user and if not there throw an exception
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Convert the comma-separated roles string into a list, strip the
        // ROLE because we will re add this when we create the mapping in the list
        List<String> roles = List.of(user.getRoles().split(","));
        String[] roleNames = roles.stream()
                .map(r -> r.replace("ROLE_", ""))
                .toArray(String[]::new);

        return User.builder()
                .username(user.getUsername())
                .password(user.getPasswordHash())
                .roles(roleNames)
                .build();
    }
}

