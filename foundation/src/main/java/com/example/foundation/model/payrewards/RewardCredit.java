package com.example.foundation.model.payrewards;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

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

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public boolean isEligible() { return eligible; }
    public void setEligible(boolean eligible) { this.eligible = eligible; }
    public LocalDateTime getDateEarned() { return dateEarned; }
    public void setDateEarned(LocalDateTime dateEarned) { this.dateEarned = dateEarned; }
    public LocalDateTime getDateApplied() { return dateApplied; }
    public void setDateApplied(LocalDateTime dateApplied) { this.dateApplied = dateApplied; }
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
}
