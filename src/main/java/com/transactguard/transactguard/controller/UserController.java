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
@PreAuthorize("hasRole('USER')")
public class UserController {

    final private UserService service;
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserProfile(@PathVariable Long id) {
        User userProfile = service.getUserProfile(id);

        if (userProfile != null) return ResponseEntity.ok(userProfile);
        else return ResponseEntity.notFound().build();
    }

    @GetMapping("/balance/{id}")
    public ResponseEntity<Double> getBalance(@PathVariable Long id) {
        Double balance = service.getBalance(id);
        if (balance != null) {
            return ResponseEntity.ok(balance);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser (@RequestBody @Valid UpdateUserDTO updateUserDTO, @PathVariable Long id) {
        try {
            User updatedUser = service.updateUser(updateUserDTO, id);
            if ( updatedUser != null) return ResponseEntity.ok("Profile Updated");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Something Went Wrong!");
        }
        return ResponseEntity.notFound().build();
    }
}
