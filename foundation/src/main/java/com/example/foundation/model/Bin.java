package com.example.foundation.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "bins")
public class Bin {

    @Id
    private String id;
    private String binCode;
    private String type;
    private String status;

    @DBRef
    private User assignedUser; 

    @DBRef
    private Tag tag;

    // --- Auto-generate unique BinCode before saving ---
    @jakarta.persistence.PrePersist
    public void prePersist() {
        if (this.binCode == null || this.binCode.isEmpty()) {
            // Generate a short random unique code (BIN-xxxx)
            this.binCode = "BIN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }

        if (this.status == null || this.status.isEmpty()) {
            this.status = "IN_STOCK"; // default status
        }
    }

    // --- Getters & Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBinCode() { return binCode; }
    public void setBinCode(String binCode) { this.binCode = binCode; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public User getAssignedUser() { return assignedUser; }
    public void setAssignedUser(User assignedUser) { this.assignedUser = assignedUser; }

    public Tag getTag() { return tag; }
    public void setTag(Tag tag) { this.tag = tag; }
}
