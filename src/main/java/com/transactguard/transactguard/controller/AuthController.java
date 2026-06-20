package com.transactguard.transactguard.controller;

import com.transactguard.transactguard.dto.LoginUserDTO;
import com.transactguard.transactguard.dto.RegisterUserDTO;
import com.transactguard.transactguard.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import com.transactguard.transactguard.entity.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    final private AuthService service;
    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser (@RequestBody @Valid RegisterUserDTO registerUserDTO) {
        service.registerUser(registerUserDTO);
        return ResponseEntity.status(201).body("Account Created");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser (@RequestBody @Valid LoginUserDTO loginUserDTO) {
        String jwtToken = service.loginUser(loginUserDTO);
        return ResponseEntity.status(200).body("Login Successful " +  jwtToken);
    }
}
