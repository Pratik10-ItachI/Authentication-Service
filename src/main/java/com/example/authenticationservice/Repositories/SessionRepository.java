package com.example.authenticationservice.Repositories;

import com.example.authenticationservice.models.Session;
import com.example.authenticationservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session,Long> {

    @Override
    public Session save(Session session);

    public Optional<Session> findByToken(String token);

    public Optional<List<Session>> findAllByUser(User user);


}
