package com.transactguard.transactguard.service;

import com.transactguard.transactguard.dto.UpdateUserDTO;
import com.transactguard.transactguard.entity.User;
import com.transactguard.transactguard.exception.RequestException;
import com.transactguard.transactguard.repo.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    final private UserRepository repository;
    final private BCryptPasswordEncoder encoder;
    public UserService(UserRepository repository, BCryptPasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public User updateUser(UpdateUserDTO dto, Long id) {
        User user = getUserById(id);

        validateAndSetUsername(user, dto.getUsername());
        validateAndSetPassword(user, dto.getPassword());
        validateAndSetEmail(user, dto.getEmail());

        return repository.save(user);
    }

    public User getUserProfile(Long id) {
        return getUserById(id);
    }

    public Double getBalance(Long id) {
        User user = getUserById(id);
        return user.getBalance();
    }

    private User getUserById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new RuntimeException("Profile with ID " + id + " not found."));
    }

    private void validateAndSetUsername(User user, String newUsername) {
        if (newUsername == null) return;
        if (newUsername.equals(user.getUsername())) {
            throw new RequestException("username", "New username must be different from your current username.");
        }
        user.setUsername(newUsername);
    }

    private void validateAndSetPassword(User user, String newPassword) {
        if (newPassword == null) return;
        if (encoder.matches(newPassword, user.getPassword())) {
            throw new RequestException("password", "New password must be different from your current password.");
        }
        user.setPassword(encoder.encode(newPassword));
    }

    private void validateAndSetEmail(User user, String newEmail) {

        if (newEmail == null) return;
        if (newEmail.equals(user.getEmail())) {
            throw new RequestException(
                    "email",
                    "New email must be different from your current email.");
        }
        if (repository.findByEmail(newEmail).isPresent()) {
            throw new RequestException(
                    "email",
                    "Email is already in use.");
        }
        user.setEmail(newEmail);
    }

}
