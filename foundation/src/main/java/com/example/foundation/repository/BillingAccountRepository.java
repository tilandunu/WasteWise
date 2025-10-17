package com.example.foundation.repository;

import com.example.foundation.model.BillingAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BillingAccountRepository extends MongoRepository<BillingAccount, String> {
    Optional<BillingAccount> findByUserId(String userId);
}
