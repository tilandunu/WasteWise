package com.example.foundation.model.payrewards;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.time.LocalDateTime;

@Document
public class Receipt {
    @Id
    private String id;
    private String transactionId;
    private LocalDateTime date;
    private String fileUrl; // PDF or JSON file location
    @DBRef
    private com.example.foundation.model.User user;
    // Getters and setters
}
