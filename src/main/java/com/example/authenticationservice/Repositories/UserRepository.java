package com.example.authenticationservice.Repositories;

import com.example.authenticationservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    public User save(User user);

    Optional<User> findByEmail(String email);

}
