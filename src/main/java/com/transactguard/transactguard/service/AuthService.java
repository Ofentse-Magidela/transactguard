package com.transactguard.transactguard.service;

import com.transactguard.transactguard.Role;
import com.transactguard.transactguard.dto.RegisterUserDTO;
import com.transactguard.transactguard.entity.User;
import com.transactguard.transactguard.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AuthService {

    @Autowired
    private UserRepository repository;

    public User registerUser(RegisterUserDTO registerUserDTO) {
        User user = new User();
        user.setUsername(registerUserDTO.getUsername());
        user.setPassword(registerUserDTO.getPassword());
        user.setEmail(registerUserDTO.getEmail());
        user.setBalance(registerUserDTO.getBalance());
        user.setCreatedAt(LocalDate.now());

        if (user.getRole() == null) user.setRole(Role.USER);
        else user.setRole(Role.ADMIN);

        return repository.save(user);
    }
}
