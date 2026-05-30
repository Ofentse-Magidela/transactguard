package com.transactguard.transactguard.repo;

import com.transactguard.transactguard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User,  Integer> {

    // Spring Boot automatically turns this into:
    // "SELECT * FROM users WHERE username = ?"
    Optional<User> findByUsername(String Username);
}
