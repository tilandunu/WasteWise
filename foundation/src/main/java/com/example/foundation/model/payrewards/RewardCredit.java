package com.example.foundation.model.payrewards;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document
public class RewardCredit {
    @Id
    private String id;
    private BigDecimal amount;
    private boolean eligible;
    private LocalDateTime dateEarned;
    private LocalDateTime dateApplied;
    @DBRef
    private Account account;
    // Getters and setters
}
