package com.example.foundation.dto.response;

public class PaymentResponse {
    private String transactionId;
    private String status;
    private double remainingBalance;

    public PaymentResponse() {}

    public PaymentResponse(String transactionId, String status, double remainingBalance) {
        this.transactionId = transactionId;
        this.status = status;
        this.remainingBalance = remainingBalance;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(double remainingBalance) {
        this.remainingBalance = remainingBalance;
    }
}
