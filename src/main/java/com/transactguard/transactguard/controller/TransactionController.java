package com.transactguard.transactguard.controller;

import com.transactguard.transactguard.dto.SendMoneyRequestDTO;
import com.transactguard.transactguard.entity.Transaction;
import com.transactguard.transactguard.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transact")
@PreAuthorize("hasRole('USER')")
public class TransactionController {

    final private TransactionService service;
    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMoney(@RequestBody @Valid SendMoneyRequestDTO sendMoneyRequest) {
        Transaction transaction = service.sendMoney(sendMoneyRequest.getSenderID(),
                                                    sendMoneyRequest.getReceiverID(),
                                                    sendMoneyRequest.getAmount());

        return ResponseEntity.status(201).body("Transaction Successful");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Transaction transaction = service.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("send/history/{id}")
    public ResponseEntity<List<Transaction>>  getTransactionHistory (@PathVariable Long id) {

        List<Transaction> transactionHistory = service.getSendTransactionHistory(id);
        return ResponseEntity.ok(transactionHistory);
    }

    @GetMapping("received/history/{id}")
    public ResponseEntity<List<Transaction>> getReceivedTransactionHistory (@PathVariable Long id) {

        List<Transaction> transactionList = service.getReceivedTransactionHistory(id);
        return ResponseEntity.ok(transactionList);

    }
}
