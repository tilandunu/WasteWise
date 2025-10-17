package com.example.foundation.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "billing_accounts")
public class BillingAccount {

    @Id
    private String id; // Mongo id
    private String userId; // reference to User.id
    private double balance; // LKR
    private double credits; // recycling credits in LKR

    public BillingAccount() {}

    public BillingAccount(String userId, double balance, double credits) {
        this.userId = userId;
        this.balance = balance;
        this.credits = credits;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getCredits() {
        return credits;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }
}
