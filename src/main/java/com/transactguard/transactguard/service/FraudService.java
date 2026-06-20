package com.transactguard.transactguard.service;

import com.transactguard.transactguard.entity.FraudFlag;
import com.transactguard.transactguard.entity.Transaction;
import com.transactguard.transactguard.entity.User;
import com.transactguard.transactguard.repo.FraudRepository;
import com.transactguard.transactguard.repo.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FraudService {

    final private FraudRepository fraudRepo;
    final private TransactionRepository transactionRepo;
    public FraudService(FraudRepository fraudRepo, TransactionRepository transactionRepo) {
        this.fraudRepo = fraudRepo;
        this.transactionRepo = transactionRepo;
    }

    public void checkFraud(Transaction transaction) {
        User user = transaction.getSender();
        Double userBalance = user.getBalance();

        Double amount = Math.abs(transaction.getAmount());

        LocalDateTime oneMinBefore = transaction.getTimestamp().minusMinutes(1);
        List<Transaction> recentTransactions = transactionRepo.findAllBySenderIdAndTimestampAfter(
                transaction.getSender().getId(),
                oneMinBefore
        );
        if (amount > 10000.00) checkLargeAmount(transaction);
        if (userBalance <= 1000 && (transaction.getAmount() / userBalance) * 100 > 70) highBalancedDrain(transaction);
        if (recentTransactions.size() >= 3) rapidTransactions(transaction);
    }

    private void rapidTransactions(Transaction transaction) {
        FraudFlag fraudFlag = new FraudFlag();

        fraudFlag.setTransaction(transaction);
        fraudFlag.setReason("Rapid Transactions");
        fraudFlag.setResolved(false);
        fraudFlag.setCreatedAt(LocalDateTime.now());
        fraudRepo.save(fraudFlag);
    }

    private void highBalancedDrain(Transaction transaction) {
        FraudFlag fraudFlag = new FraudFlag();

        fraudFlag.setTransaction(transaction);
        fraudFlag.setReason("High Balance Drain");
        fraudFlag.setResolved(false);
        fraudFlag.setCreatedAt(LocalDateTime.now());
        fraudRepo.save(fraudFlag);
    }

    private void checkLargeAmount(Transaction transaction) {
        FraudFlag fraudFlag = new FraudFlag();

        fraudFlag.setTransaction(transaction);
        fraudFlag.setReason("Amount exceeds limit");
        fraudFlag.setResolved(false);
        fraudFlag.setCreatedAt(LocalDateTime.now());
        fraudRepo.save(fraudFlag);
    }

}
