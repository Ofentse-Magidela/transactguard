package com.transactguard.transactguard.controller;

import com.transactguard.transactguard.dto.SendMoneyRequestDTO;
import com.transactguard.transactguard.entity.Transaction;
import com.transactguard.transactguard.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transact")
public class TransactionController {

    @Autowired
    TransactionService service;

    @PostMapping("/send")
    public String sendMoney(@RequestBody SendMoneyRequestDTO sendMoneyRequest) {
        Transaction transaction = service.sendMoney(sendMoneyRequest.getSenderID(),
                                                    sendMoneyRequest.getReceiverID(),
                                                    sendMoneyRequest.getAmount());
        if (transaction == null) return "Check if you have enough funds, or your id and sender id is correct";
        else return "funds transferred successfully"; // again testing
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable Long id) {
        return service.getTransactionById(id);
    }

    @GetMapping("send/history/{id}")
    public List<Transaction>  getTransactionHistory (@PathVariable Long id) {
        return service.getSendTransactionHistory(id);
    }
    @GetMapping("received/history/{id}")
    public List<Transaction>  getReceivedTransactionHistory (@PathVariable Long id) {
        return service.getReceivedTransactionHistory(id);
    }
}
