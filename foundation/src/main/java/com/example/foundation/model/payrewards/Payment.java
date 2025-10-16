package com.example.foundation.model.payrewards;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.example.foundation.model.payrewards.PaymentMethod;
import com.example.foundation.model.payrewards.Receipt;

@Document
public class Payment {
    @Id
    private String id;
    private BigDecimal amount;
    @DBRef
    private PaymentMethod paymentMethod;
    private String transactionId;
    private LocalDateTime date;
    private PaymentStatus status;
    @DBRef
    private Receipt receipt;
    @DBRef
    private Account account;
    // Getters and setters
}

enum PaymentStatus {
    PENDING,
    SUCCESS,
    FAILED
}
