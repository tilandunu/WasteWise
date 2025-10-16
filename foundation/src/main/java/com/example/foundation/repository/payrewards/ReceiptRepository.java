package com.example.foundation.repository.payrewards;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.foundation.model.payrewards.Receipt;

public interface ReceiptRepository extends MongoRepository<Receipt, String> {
    List<Receipt> findByUser_Id(String userId);
    Receipt findByTransactionId(String transactionId);
}
