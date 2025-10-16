package com.example.foundation.repository.payrewards;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.foundation.model.payrewards.Account;

public interface AccountRepository extends MongoRepository<Account, String> {
    Account findByUser_Id(String userId);
}
