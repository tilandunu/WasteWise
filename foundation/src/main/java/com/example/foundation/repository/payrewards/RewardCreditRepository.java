package com.example.foundation.repository.payrewards;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.foundation.model.payrewards.RewardCredit;

public interface RewardCreditRepository extends MongoRepository<RewardCredit, String> {
    List<RewardCredit> findByAccount_User_IdAndEligibleTrue(String userId);
}
