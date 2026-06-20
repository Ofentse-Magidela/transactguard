package com.transactguard.transactguard.controller;

import com.transactguard.transactguard.entity.FraudFlag;
import com.transactguard.transactguard.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    final private AdminService service;
    public AdminController(AdminService service) {
        this.service = service;
    }

    @GetMapping("/flags")
    public ResponseEntity<List<FraudFlag>> getAllFlags() {
        List<FraudFlag> fraudFlags = service.getAllFlags();
        return ResponseEntity.ok(fraudFlags);
    }


    @PostMapping("/resolve/{id}")
    public ResponseEntity<String> resolveFlags(@PathVariable Long id) {
        boolean isResolved = service.resolveFlag(id);
        if (isResolved) return ResponseEntity.status(201).body("SUCCESS");
        return ResponseEntity.notFound().build();
    }
}
