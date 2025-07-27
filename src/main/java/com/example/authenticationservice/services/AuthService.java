package com.example.authenticationservice.services;

import com.example.authenticationservice.Exceptions.IncorrectPasswordException;
import com.example.authenticationservice.Exceptions.UserAlreadyExistsException;
import com.example.authenticationservice.Exceptions.UserNotFoundException;
import com.example.authenticationservice.Repositories.SessionRepository;
import com.example.authenticationservice.Repositories.UserRepository;
import com.example.authenticationservice.models.Session;
import com.example.authenticationservice.models.SessionStatus;
import com.example.authenticationservice.models.User;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Getter
@Setter
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SecretKey secretKey = Jwts.SIG.HS256.key().build();
    private final SessionRepository sessionRepository;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                       SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.sessionRepository = sessionRepository;
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
            String token =  createJwtToken(user.get().getId(), new ArrayList<>(),user.get().getEmail());

            Session session = new Session();
            session.setToken(token);
            session.setUser(user.get());

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 30);
            Date expirationDate = calendar.getTime();

            session.setExpireAt(expirationDate);
            session.setStatus(SessionStatus.ACTIVE);

            sessionRepository.save(session);

            return token;
        }
        else{
            throw new IncorrectPasswordException();
        }

    }

    private String createJwtToken(Long User_ID, List<String> roles,String email){

        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("User_Id", User_ID);
        claims.put("roles", roles);
        claims.put("email",email);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date expirationDate = calendar.getTime();

        return Jwts.builder()
                .claims(claims)
                .issuer("Pratik.dev")
                .expiration(expirationDate)
                .issuedAt(new Date())
                .signWith(secretKey)
                .compact();
    }

    public Boolean validate(String token) {
        Jws<Claims> jws;

        try {
            jws = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

            Date expiration = jws.getPayload().getExpiration();
            Date issued = jws.getPayload().getIssuedAt();
            String issuer = jws.getPayload().getIssuer();
            Long UserId = jws.getPayload().get("User_Id", Long.class);

            Optional<Session> session = sessionRepository.findByToken(token);
            if (session.isPresent() && session.get().getStatus().equals(SessionStatus.ACTIVE)) {
                return true;
            }
            else{
                throw new UserNotFoundException();
            }

        }catch (JwtException | UserNotFoundException ex) {
                return false;
        }
    }

    public Boolean logout(String email) throws UserNotFoundException {

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        Optional<List<Session>> sessions = sessionRepository.findAllByUser(user.get());
        if (sessions.isPresent()) {
            for (Session session : sessions.get()) {
                session.setStatus(SessionStatus.ENDED);
                sessionRepository.save(session);
            }
            return true;
        } else {
            throw new UserNotFoundException();
        }
    }

}
