package com.example.foundation.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.foundation.model.BillingAccount;

public interface BillingAccountRepository extends MongoRepository<BillingAccount, String> {
    Optional<BillingAccount> findByUserId(String userId);
}
