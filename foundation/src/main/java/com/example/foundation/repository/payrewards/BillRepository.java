package com.example.foundation.repository.payrewards;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.foundation.model.payrewards.Bill;

public interface BillRepository extends MongoRepository<Bill, String> {
    List<Bill> findByAccount_User_Id(String userId);
    Bill findByAccount_IdAndBillingPeriod(String accountId, String billingPeriod);
}
