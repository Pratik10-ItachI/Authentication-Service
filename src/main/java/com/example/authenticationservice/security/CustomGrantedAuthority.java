package com.example.authenticationservice.security;

import com.example.authenticationservice.models.Role;
import org.springframework.security.core.GrantedAuthority;

public class CustomGrantedAuthority implements GrantedAuthority {
    private String authority;

public CustomGrantedAuthority(Role role) {
        this.authority = role.getRole();
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
