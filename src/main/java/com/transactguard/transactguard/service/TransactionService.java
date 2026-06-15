package com.transactguard.transactguard.service;

import com.transactguard.transactguard.TransactionStatus;
import com.transactguard.transactguard.entity.Transaction;
import com.transactguard.transactguard.entity.User;
import com.transactguard.transactguard.repo.TransactionRepository;
import com.transactguard.transactguard.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    final private TransactionRepository transactRepo;
    final private UserRepository userRepo;

    public TransactionService(TransactionRepository transactRepo, UserRepository userRepo) {
        this.transactRepo = transactRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public Transaction sendMoney(Long senderID, Long receiverID, Double amount) {

        Transaction transaction = new Transaction();

        User sender;
        User receiver;

        Optional<User> optionalSender = userRepo.findById(senderID);
        Optional<User> optionalReceiver = userRepo.findById(receiverID);

        if (optionalSender.isPresent() && optionalReceiver.isPresent()) {
            sender = optionalSender.get();
            receiver = optionalReceiver.get();
        }
        else return null;

        if (sender.getBalance() >= amount) {

            sender.setBalance(sender.getBalance() - amount);
            receiver.setBalance(receiver.getBalance() + amount);

            transaction.setAmount(amount);
            transaction.setStatus(TransactionStatus.SUCCESSFUL);
            transaction.setTimestamp(LocalDateTime.now());

            sender.setSenderTransactions(transaction);
            receiver.setReceiverTransactions(transaction);
        }
        else {

            transaction.setAmount(0.00);
            transaction.setStatus(TransactionStatus.CANCELED);
            transaction.setTimestamp(LocalDateTime.now());
            sender.setSenderTransactions(transaction);

            return null;//practice purpose will handle if I see this approach is working
        }
        transaction.setSender(sender);
        transaction.setReceiver(receiver);

        transactRepo.save(transaction);
        return transaction;

    }

    public Transaction getTransactionById(Long id) {
        Optional<Transaction> transaction = transactRepo.findById(id);
        return transaction.orElse(null);
    }

    public List<Transaction> getSendTransactionHistory(Long id) {
        return transactRepo.findAllBySenderId(id);
    }
    public List<Transaction> getReceivedTransactionHistory(Long id) {
        return transactRepo.findAllByReceiverId(id);
    }
}


