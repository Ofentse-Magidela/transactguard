package com.transactguard.transactguard.service;

import com.transactguard.transactguard.entity.FraudFlag;
import com.transactguard.transactguard.repo.FraudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    final private FraudRepository repository;
    public AdminService(FraudRepository repository) {
        this.repository = repository;
    }

    public List<FraudFlag> getAllFlags() {
        return repository.findAllByResolved(false);
    }

    public boolean resolveFlag(Long id) {

        Optional<FraudFlag> fraudFlagOptional = repository.findById(id);
        if(fraudFlagOptional.isPresent()) {

            FraudFlag fraudFlag = fraudFlagOptional.get();
            fraudFlag.setResolved(true);
            repository.save(fraudFlag);

            return true;
        }
        return false;
    }
}
