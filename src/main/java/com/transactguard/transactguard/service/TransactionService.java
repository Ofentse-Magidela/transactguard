package com.transactguard.transactguard.service;

import com.transactguard.transactguard.entity.Transaction;
import com.transactguard.transactguard.repo.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository repo;

    public Transaction getBalance() {
        return repo.findById(1).orElse(null); //just testing for now
    }
}
