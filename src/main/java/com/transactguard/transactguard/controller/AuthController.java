package com.transactguard.transactguard.controller;

import com.transactguard.transactguard.dto.LoginUserDTO;
import com.transactguard.transactguard.dto.RegisterUserDTO;
import com.transactguard.transactguard.service.AuthService;
import jakarta.validation.Valid;
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
    private AuthService service;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser (@RequestBody @Valid RegisterUserDTO registerUserDTO) {
        try {
            User addedUser = service.registerUser(registerUserDTO);
            return ResponseEntity.status(201).body("Account Created");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Something Went Wrong!");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser (@RequestBody @Valid LoginUserDTO loginUserDTO) {

        if(service.loginUser(loginUserDTO) == null)
            return ResponseEntity.status(201).body("Login Successful");
        return ResponseEntity.badRequest().body("Something Went Wrong!");
    }
}
