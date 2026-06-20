package com.transactguard.transactguard.repo;

import com.transactguard.transactguard.entity.FraudFlag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FraudRepository extends JpaRepository<FraudFlag, Long> {
    List<FraudFlag> findAllByResolved(boolean resolved);
}
