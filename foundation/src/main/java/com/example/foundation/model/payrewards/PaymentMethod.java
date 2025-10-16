package com.example.foundation.model.payrewards;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Document
public class PaymentMethod {
    @Id
    private String id;
    private String type; // CARD or BANK_TRANSFER
    private String cardHolderName;
    private String cardLast4Digits;
    private String cardExpiry;
    private String bankName;
    private String bankAccountNumber;
    @DBRef
    private com.example.foundation.model.User user;
    // Getters and setters
}
