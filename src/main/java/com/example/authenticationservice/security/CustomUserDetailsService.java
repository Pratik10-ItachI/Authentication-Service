package com.example.authenticationservice.security;

import com.example.authenticationservice.Repositories.UserRepository;
import com.example.authenticationservice.models.User;
import com.example.authenticationservice.security.services.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    private String username;

    public CustomUserDetailsService(CustomUserDetails customUserDetails, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.username = customUserDetails.getUsername();


    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByEmail(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        return new CustomUserDetails(user.get());
    }
}
