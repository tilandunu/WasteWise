package com.example.foundation.model.payrewards;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.math.BigDecimal;
import java.util.List;
import com.example.foundation.model.User;

@Document
public class Account {
    @Id
    private String id;
    private BigDecimal balance;
    @DBRef
    private User user;
    @DBRef
    private List<Payment> payments;
    @DBRef
    private List<RewardCredit> rewardCredits;
    // Getters and setters
}
