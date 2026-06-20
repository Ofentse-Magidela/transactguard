package com.transactguard.transactguard.service;

import com.transactguard.transactguard.dto.UpdateUserDTO;
import com.transactguard.transactguard.entity.User;
import com.transactguard.transactguard.repo.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    final private UserRepository repository;
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User updateUser(UpdateUserDTO updateUserDTO, Long id) {
        User user = repository.findById(id).orElseThrow(() ->
                new RuntimeException("Profile with ID " + id + " not found."));

        user.setUsername(updateUserDTO.getUsername());
        user.setPassword(updateUserDTO.getPassword());
        user.setEmail(updateUserDTO.getEmail());
        repository.save(user);
        return user;
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
