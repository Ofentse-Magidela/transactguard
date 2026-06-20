package com.transactguard.transactguard.service;

import com.transactguard.transactguard.entity.FraudFlag;
import com.transactguard.transactguard.repo.FraudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    final private FraudRepository repository;
    public AdminService(FraudRepository repository) {
        this.repository = repository;
    }

    public List<FraudFlag> getAllFlags() {
        return repository.findAllByResolved(false);
    }

    public void resolveFlag(Long id) {

        FraudFlag fraudFlag = repository.findById(id).orElseThrow(() ->
                new RuntimeException("FraudFlag with ID " + id + " not found."));

        fraudFlag.setResolved(true);
        repository.save(fraudFlag);
    }
}
