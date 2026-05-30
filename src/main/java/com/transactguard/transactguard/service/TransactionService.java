package com.transactguard.transactguard.service;

import com.transactguard.transactguard.TransactionStatus;
import com.transactguard.transactguard.entity.Transaction;
import com.transactguard.transactguard.entity.User;
import com.transactguard.transactguard.repo.TransactionRepository;
import com.transactguard.transactguard.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactRepo;

    @Autowired
    UserRepository userRepo;

    public Transaction getBalance() {
        return transactRepo.findById(1).orElse(null); //just testing for now
    }

    @Transactional
    public Transaction sendMoney(String sender, String receiver, Double amount) {

        Transaction transaction = new Transaction();
        User sender1;
        User receiver1;

        Optional<User> optionalSender = userRepo.findByUsername(sender);
        Optional<User> optionalReceiver = userRepo.findByUsername(receiver);

        if (optionalSender.isPresent() && optionalReceiver.isPresent()) {
            sender1 = optionalSender.get();
            receiver1 = optionalReceiver.get();
        }
        else return null;

        if (sender1.getBalance() >= amount) {

            sender1.setBalance(sender1.getBalance() - amount);
            receiver1.setBalance(receiver1.getBalance() + amount);

            transaction.setStatus(TransactionStatus.SUCCESSFUL);
            transaction.setTimestamp(LocalTime.now());

            sender1.setTransaction(transaction);
            receiver1.setTransaction(transaction);
        }
        else {
            transaction.setStatus(TransactionStatus.CANCELED);
            transaction.setTimestamp(LocalTime.now());
            sender1.setTransaction(transaction);

            return null;//practice purpose will handle if I see this approach is working
        }
        transaction.setSender(sender);
        transaction.setReceiver(receiver);

        return transaction;

    }
}
