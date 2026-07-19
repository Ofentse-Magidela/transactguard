package com.transactguard.transactguard.repo;

import com.transactguard.transactguard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository <User,  Long> {

    // "SELECT * FROM users WHERE username = ?"
    Optional<User> findByUsername(String Username);
    Optional<User> findByEmail (String Email);
}
