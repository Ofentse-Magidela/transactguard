package com.transactguard.transactguard.repo;

import com.transactguard.transactguard.entity.FraudFlag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FraudRepository extends JpaRepository<FraudFlag, Integer> {
}
