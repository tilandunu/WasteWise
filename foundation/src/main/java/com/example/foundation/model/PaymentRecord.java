package com.example.foundation.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "payments")
public class PaymentRecord {

    public enum Status { SUCCESS, FAILED, PENDING }

    @Id
    private String id;

    private String transactionId;
    private String userId; // resident/user id
    private double amount;
    private String method; // CARD or BANK or CREDIT
    private Status status;
    private Instant timestamp;
    private String note;

    public PaymentRecord() {}

    public PaymentRecord(String transactionId, String userId, double amount, String method, Status status, Instant timestamp, String note) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.timestamp = timestamp;
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
