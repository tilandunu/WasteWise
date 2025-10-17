package com.example.foundation.repository;

import com.example.foundation.model.PaymentRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PaymentRecordRepository extends MongoRepository<PaymentRecord, String> {
    List<PaymentRecord> findByUserId(String userId);
}
