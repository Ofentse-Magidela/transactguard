package com.transactguard.transactguard.repo;

import com.transactguard.transactguard.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllBySenderId(Long id);
    List<Transaction> findAllByReceiverId(Long id);
    List<Transaction> findAllBySenderIdAndTimestampAfter(Long id, LocalDateTime oneMinBefore);
}
