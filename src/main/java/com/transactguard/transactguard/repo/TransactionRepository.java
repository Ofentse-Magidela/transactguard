package com.transactguard.transactguard.repo;

import com.transactguard.transactguard.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
