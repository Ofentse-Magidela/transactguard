package com.transactguard.transactguard.service;

import com.transactguard.transactguard.dto.UpdateUserDTO;
import com.transactguard.transactguard.entity.User;
import com.transactguard.transactguard.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User updateUser(UpdateUserDTO updateUserDTO, Long id) {
        Optional<User> optionalUser = repository.findById(id);
        User user;

        if(optionalUser.isPresent()) {

            user = optionalUser.get();
            user.setUsername(updateUserDTO.getUsername());
            user.setPassword(updateUserDTO.getPassword());
            user.setEmail(updateUserDTO.getEmail());
            repository.save(user);
            return user;
        }
        else return null;
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
