package com.transactguard.transactguard.controller;


import com.transactguard.transactguard.entity.Transaction;
import com.transactguard.transactguard.entity.User;
import com.transactguard.transactguard.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transact")
public class TransactionController {

    @Autowired
    TransactionService service;

    @PostMapping("/send")
    public String sendMoney(String sender, String receiver, double amount) {
        Transaction transaction = service.sendMoney(sender, receiver, amount);
        if (transaction == null) return "Check if you have enough funds, or your id and sender id is correct";
        else return "funds transferred successfully"; // again testing
    }

    @PostMapping("/receive")
    public Double what () {
        //It will execute 100% for testing of course
        if(service.getBalance() == null) return 0.0;
        return null;
    }
}
