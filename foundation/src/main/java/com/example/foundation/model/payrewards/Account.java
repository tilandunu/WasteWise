package com.example.foundation.model.payrewards;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

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

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public List<Payment> getPayments() { return payments; }
    public void setPayments(List<Payment> payments) { this.payments = payments; }
    public List<RewardCredit> getRewardCredits() { return rewardCredits; }
    public void setRewardCredits(List<RewardCredit> rewardCredits) { this.rewardCredits = rewardCredits; }
}
