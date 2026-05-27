package com.transactguard.transactguard.service;
import com.transactguard.transactguard.entity.User;
import com.transactguard.transactguard.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User updateUser(int id,User user) {
        User updateUser = repository.findById(id).orElse(null);
        if (updateUser != null) {
            user.setId(id);
            repository.save(user);
        }
        return updateUser;
    }
    public User getUserProfile(int id) {
        return repository.findById(id).orElse(null);
    }

    public User addUser(User user) {
        return repository.save(user);
    }

    public Double getBalance(int id) {
        User user = repository.findById(id).orElse(null);
        if ( user != null) {
            return user.getBalance();
        }
        return null;
    }
}
