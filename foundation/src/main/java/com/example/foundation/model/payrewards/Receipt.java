package com.example.foundation.model.payrewards;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Receipt {
    @Id
    private String id;
    private String transactionId;
    private LocalDateTime date;
    private String fileUrl; // PDF or JSON file location
    @DBRef
    private com.example.foundation.model.User user;
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public com.example.foundation.model.User getUser() { return user; }
    public void setUser(com.example.foundation.model.User user) { this.user = user; }
}
