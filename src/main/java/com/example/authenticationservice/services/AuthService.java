package com.example.authenticationservice.services;

import com.example.authenticationservice.Exceptions.IncorrectPasswordException;
import com.example.authenticationservice.Exceptions.UserAlreadyExistsException;
import com.example.authenticationservice.Exceptions.UserNotFoundException;
import com.example.authenticationservice.Repositories.UserRepository;
import com.example.authenticationservice.models.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Getter
@Setter
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Boolean signUp(String email, String password) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException(email);
        }
        else {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(bCryptPasswordEncoder.encode(password));
            userRepository.save(newUser);
            return true;
        }
    }

    public String LogIn(String email, String password) throws UserNotFoundException, IncorrectPasswordException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        if (bCryptPasswordEncoder.matches(password, user.get().getPassword())) {
            return "token";
        }
        else{
            throw new IncorrectPasswordException();
        }

    }

}
