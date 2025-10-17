package com.example.foundation.dto.request;

public class PaymentRequest {
    private String method; // CARD or BANK
    private double amount; // in LKR
    private String details; // simple free-text for card/bank details

    public PaymentRequest() {}

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
