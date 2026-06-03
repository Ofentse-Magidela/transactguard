package com.transactguard.transactguard.service;

import com.transactguard.transactguard.entity.User;
import com.transactguard.transactguard.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User updateUser(Long id, User user) {
        User updatedUser = repository.findById(id).orElse(null);
        if (updatedUser != null) {
            user.setId(id);
            repository.save(user);
        }
        return updatedUser;
    }

    public User getUserProfile(Long id) {
        return repository.findById(id).orElse(null);
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username).
                orElse(null); // for practise
    }

    public Double getBalance(Long id) {
        User user = repository.findById(id).orElse(null);
        if ( user != null) {
            return user.getBalance();
        }
        return null;
    }
}
