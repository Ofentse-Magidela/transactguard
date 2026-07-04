package com.transactguard.transactguard.controller;

import com.transactguard.transactguard.dto.UpdateUserDTO;
import com.transactguard.transactguard.entity.User;
import com.transactguard.transactguard.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('USER', 'ADMIN')")
public class UserController {

    final private UserService service;
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserProfile(@PathVariable Long id) {

        User userProfile = service.getUserProfile(id);
        return ResponseEntity.ok(userProfile);
    }

    @GetMapping("/balance/{id}")
    public ResponseEntity<Double> getBalance(@PathVariable Long id) {

        Double balance = service.getBalance(id);
        return ResponseEntity.ok(balance);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<String> updateUser (@RequestBody @Valid UpdateUserDTO updateUserDTO, @PathVariable Long id) {

        User updatedUser = service.updateUser(updateUserDTO, id);
        return ResponseEntity.ok("Profile Updated " + updatedUser);

    }
}
