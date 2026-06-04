package com.transactguard.transactguard.controller;

import com.transactguard.transactguard.dto.SendMoneyRequestDTO;
import com.transactguard.transactguard.entity.Transaction;
import com.transactguard.transactguard.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transact")
public class TransactionController {

    @Autowired
    TransactionService service;

    @PostMapping("/send")
    public ResponseEntity<String> sendMoney(@RequestBody @Valid SendMoneyRequestDTO sendMoneyRequest) {
        Transaction transaction = service.sendMoney(sendMoneyRequest.getSenderID(),
                                                    sendMoneyRequest.getReceiverID(),
                                                    sendMoneyRequest.getAmount());

        if (transaction != null) return ResponseEntity.status(201).body("funds transferred successfully");
        return ResponseEntity.badRequest().body("Check if you have enough funds, or your id and sender id is correct");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Transaction t = service.getTransactionById(id);
        if (t != null) return ResponseEntity.ok(t);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("send/history/{id}")
    public ResponseEntity<List<Transaction>>  getTransactionHistory (@PathVariable Long id) {
        List<Transaction> t = service.getSendTransactionHistory(id);

        if (t != null) return ResponseEntity.ok(t);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("received/history/{id}")
    public ResponseEntity<List<Transaction>> getReceivedTransactionHistory (@PathVariable Long id) {
        List<Transaction> t = service.getReceivedTransactionHistory(id);

        if (t != null) return ResponseEntity.ok(t);
        return ResponseEntity.notFound().build();
    }
}
