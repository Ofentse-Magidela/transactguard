package com.transactguard.transactguard.service;

import com.transactguard.transactguard.entity.Transaction;
import com.transactguard.transactguard.entity.User;
import com.transactguard.transactguard.repo.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository repo;

    @Autowired
    UserService service;

    public Transaction getBalance() {
        return repo.findById(1).orElse(null); //just testing for now
    }

    public Transaction sendMoney(String sender, String receiver, double amount) {
        Transaction transaction = new Transaction();

        User sender1 = service.getByUsername(sender);
        User receiver1 = service.getByUsername(receiver);

        if (sender1 == null || receiver1 == null) return null;// same here


        if (sender1.getBalance() >= amount) {

            sender1.setBalance(sender1.getBalance() - amount);
            receiver1.setBalance(receiver1.getBalance() + amount);

            transaction.setStatus(true);
            transaction.setTimestamp(LocalTime.now());

            sender1.setTransaction(transaction);
            receiver1.setTransaction(transaction);
        }
        else {
            transaction.setStatus(false);
            transaction.setTimestamp(LocalTime.now());
            sender1.setTransaction(transaction);

            return null;//practice purpose will handle if I see this approach is working
        }
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        repo.save(transaction);

        return transaction;

    }
}
