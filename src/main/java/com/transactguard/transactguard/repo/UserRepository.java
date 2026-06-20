package com.transactguard.transactguard.repo;

import com.transactguard.transactguard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface UserRepository extends JpaRepository <User,  Long> {

    // Spring Boot automatically turns this into:
    // "SELECT * FROM users WHERE username = ?"
    Optional<User> findByUsername(String Username);
}
