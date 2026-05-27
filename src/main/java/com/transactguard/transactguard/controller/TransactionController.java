package com.transactguard.transactguard.controller;

import com.transactguard.transactguard.entity.Transaction;
import com.transactguard.transactguard.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transact")
public class TransactionController {

    @Autowired
    TransactionService service;

    @GetMapping
    public Double testing () {
        //It will execute 100% for testing of course
        if(service.getBalance() == null) return 0.0;
        return null;
    }
}
