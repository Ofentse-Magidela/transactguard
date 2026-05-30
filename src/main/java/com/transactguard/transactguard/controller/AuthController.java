package com.transactguard.transactguard.controller;

import com.transactguard.transactguard.service.AuthService;
import org.springframework.http.ResponseEntity;
import com.transactguard.transactguard.entity.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService service;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser (@RequestBody User user) {
        try {
            User addedUser = service.registerUser(user);
            return ResponseEntity.status(201).body("Profile Updated");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Something Went Wrong!");
        }
    }
}
