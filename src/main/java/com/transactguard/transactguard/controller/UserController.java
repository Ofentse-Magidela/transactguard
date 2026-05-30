package com.transactguard.transactguard.controller;

import com.transactguard.transactguard.entity.User;
import com.transactguard.transactguard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService service;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserProfile(@PathVariable int id) {
        User userProfile = service.getUserProfile(id);

        if (userProfile != null) return ResponseEntity.ok(userProfile);
        else return ResponseEntity.notFound().build();
    }

    @GetMapping("/balance/{id}")
    public ResponseEntity<Double> getBalance(@PathVariable int id) {
        Double balance = service.getBalance(id);
        if (balance != null) {
            return ResponseEntity.ok(balance);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser (@RequestBody User user, @PathVariable int id) {
        try {
            User updatedUser = service.updateUser(id, user);
            if ( updatedUser != null) return ResponseEntity.ok("Profile Updated");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Something Went Wrong!");
        }
        return ResponseEntity.notFound().build();
    }
}
