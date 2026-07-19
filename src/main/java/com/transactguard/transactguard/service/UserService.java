package com.transactguard.transactguard.service;

import com.transactguard.transactguard.dto.UpdateUserDTO;
import com.transactguard.transactguard.entity.User;
import com.transactguard.transactguard.repo.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {


    final private UserRepository repository;
    final private BCryptPasswordEncoder encoder;
    public UserService(UserRepository repository, BCryptPasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public User updateUser(UpdateUserDTO updateUserDTO, Long id) {
        User user = repository.findById(id).orElseThrow(() ->
                new RuntimeException("Profile with ID " + id + " not found."));

        if (updateUserDTO.getUsername() != null && !updateUserDTO.getUsername().equals(user.getUsername()))
            user.setUsername(updateUserDTO.getUsername());
        else throw new RuntimeException("Username must not be the same or empty");
        if (updateUserDTO.getPassword() != null) user.setPassword(encoder.encode(updateUserDTO.getPassword()));

        if (updateUserDTO.getEmail() != null) {

            if (user.getEmail().equals(updateUserDTO.getEmail())) throw new RuntimeException("Enter a different email");
            Optional<User> existEmail = repository.findByEmail(updateUserDTO.getEmail());

            if (existEmail.isEmpty()) user.setEmail(updateUserDTO.getEmail());
            else throw new RuntimeException("Email already exist");
        }

        return repository.save(user);
    }

    public User getUserProfile(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new RuntimeException("Profile with ID " + id + " not found."));
    }

    public Double getBalance(Long id) {
        User user = repository.findById(id).orElseThrow(() ->
                new RuntimeException("Profile with ID " + id + " not found."));
        return user.getBalance();
    }
}
