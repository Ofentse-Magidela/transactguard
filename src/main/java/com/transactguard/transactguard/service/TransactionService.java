package com.transactguard.transactguard.service;

import com.transactguard.transactguard.TransactionStatus;
import com.transactguard.transactguard.entity.Transaction;
import com.transactguard.transactguard.entity.User;
import com.transactguard.transactguard.exception.RequestException;
import com.transactguard.transactguard.repo.TransactionRepository;
import com.transactguard.transactguard.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class  TransactionService {

    final private TransactionRepository transactRepo;
    final private UserRepository userRepo;
    final private FraudService fraudService;

    public TransactionService(TransactionRepository transactRepo, UserRepository userRepo, FraudService fraudService) {
        this.transactRepo = transactRepo;
        this.userRepo = userRepo;
        this.fraudService = fraudService;
    }

    @Transactional
    public Transaction sendMoney(Long senderID, Long receiverID, Double amount) {

        if (senderID.equals(receiverID)) throw new RequestException("receiverID","Cannot send money to yourself");

        Transaction transaction = new Transaction();

        User sender = userRepo.findById(senderID).orElseThrow(() ->
                new RuntimeException("Profile with ID " + senderID + " not found."));
        User receiver = userRepo.findById(receiverID).orElseThrow(() ->
                new RuntimeException("Profile with ID " + receiverID + " not found."));

        if (sender.getBalance() < amount) {

            transaction.setAmount(-amount);
            transaction.setStatus(TransactionStatus.CANCELED);

            transaction.setTimestamp(LocalDateTime.now());
            sender.setSenderTransactions(transaction);
            transaction.setSender(sender);
            transaction.setReceiver(receiver);

            transactRepo.save(transaction);
            fraudService.checkFraud(transaction);

            throw new RequestException("amount", "Insufficient funds.");
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        transaction.setAmount(amount);
        transaction.setStatus(TransactionStatus.SUCCESSFUL);
        transaction.setTimestamp(LocalDateTime.now());

        sender.setSenderTransactions(transaction);
        receiver.setReceiverTransactions(transaction);

        transaction.setSender(sender);
        transaction.setReceiver(receiver);

        transactRepo.save(transaction);
        fraudService.checkFraud(transaction);
        return transaction;

    }

    public Transaction getTransactionById(Long id) {
        return transactRepo.findById(id).orElseThrow(() ->
                new RuntimeException("Transaction with ID " + id + " not found."));
    }

    public List<Transaction> getSendTransactionHistory(Long id) {
        return transactRepo.findAllBySenderId(id);
    }

    public List<Transaction> getReceivedTransactionHistory(Long id) {
        return transactRepo.findAllByReceiverId(id);
    }

    public Map<String, List<Transaction>> getTransactionHistory(Long id) {
        Map<String, List<Transaction>> history = new HashMap<>();
        history.put("sent", getSendTransactionHistory(id));
        history.put("received", getReceivedTransactionHistory(id));
        return history;
    }


}


