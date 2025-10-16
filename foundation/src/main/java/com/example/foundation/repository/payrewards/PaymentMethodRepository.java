package com.example.foundation.repository.payrewards;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.foundation.model.payrewards.PaymentMethod;

public interface PaymentMethodRepository extends MongoRepository<PaymentMethod, String> {
    List<PaymentMethod> findByUser_Id(String userId);
}
