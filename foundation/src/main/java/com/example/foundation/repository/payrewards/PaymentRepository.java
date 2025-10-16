package com.example.foundation.repository.payrewards;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.foundation.model.payrewards.Payment;

public interface PaymentRepository extends MongoRepository<Payment, String> {
    List<Payment> findByAccount_User_Id(String userId);
    Payment findByTransactionId(String transactionId);
}
