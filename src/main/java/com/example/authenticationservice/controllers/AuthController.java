package com.example.authenticationservice.controllers;

import com.example.authenticationservice.Exceptions.IncorrectPasswordException;
import com.example.authenticationservice.Exceptions.UserAlreadyExistsException;
import com.example.authenticationservice.Exceptions.UserNotFoundException;
import com.example.authenticationservice.dtos.*;
import com.example.authenticationservice.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/Auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/SignUp")
    public ResponseEntity<SignupResponseDto> Signup(@RequestBody SignupRequestDto signupRequestDto) throws UserAlreadyExistsException {
        SignupResponseDto signupResponseDto = new SignupResponseDto();
        try {
            if (authService.signUp(signupRequestDto.getEmail(), signupRequestDto.getPassword())) {
                signupResponseDto.setStatus(RequestStatus.SUCCESS);
                return ResponseEntity.ok(signupResponseDto);
            } else {
                signupResponseDto.setStatus(RequestStatus.FAILURE);
                return ResponseEntity.badRequest().body(signupResponseDto);
            }
        } catch(UserAlreadyExistsException e) {
            signupResponseDto.setStatus(RequestStatus.FAILURE);
            signupResponseDto.setMessage(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    };

    @PostMapping("/Login")
    public ResponseEntity<LoginResponseDto> Login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        try {
            String token = authService.LogIn(loginRequestDto.getEmail(), loginRequestDto.getPassword());
            if (token != null) {
                loginResponseDto.setStatus(RequestStatus.SUCCESS);
                MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
                headers.add("Auth_token", token);
                return new ResponseEntity<>(loginResponseDto, headers, HttpStatus.OK);
            } else {
                loginResponseDto.setStatus(RequestStatus.FAILURE);
                return ResponseEntity.badRequest().body(loginResponseDto);
            }
        } catch (UserNotFoundException | IncorrectPasswordException e){
            loginResponseDto.setStatus(RequestStatus.FAILURE);
            loginResponseDto.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(loginResponseDto);
        }

    };
}
