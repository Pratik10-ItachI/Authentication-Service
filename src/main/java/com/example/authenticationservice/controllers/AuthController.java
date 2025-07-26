package com.example.authenticationservice.controllers;

import com.example.authenticationservice.dtos.LoginRequestDto;
import com.example.authenticationservice.dtos.LoginResponseDto;
import com.example.authenticationservice.dtos.SignupRequestDto;
import com.example.authenticationservice.dtos.SignupResponseDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/Auth")
public class AuthController {

    @PostMapping("/SignUp")
    public SignupResponseDto Signup(SignupRequestDto signupRequestDto) {
        return new SignupResponseDto();
    };

    @PostMapping("/Login")
    public LoginResponseDto Login(LoginRequestDto loginRequestDto) {
        return new LoginResponseDto();
    };
}
