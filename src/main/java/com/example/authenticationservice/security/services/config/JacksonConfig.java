package com.example.authenticationservice.security.services.config;

import com.example.authenticationservice.security.services.CustomUserDetails;
import com.example.authenticationservice.security.services.CustomUserDetailsMixin;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(CustomUserDetails.class, CustomUserDetailsMixin.class);
        return mapper;
    }
}
