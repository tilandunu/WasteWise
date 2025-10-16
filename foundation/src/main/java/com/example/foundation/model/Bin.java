package com.example.foundation.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bins")
public class Bin {

    @Id
    private String id;
    private String type;      // e.g., General, Recyclable
    private String status;    // e.g., IN_STOCK, ASSIGNED

    @DBRef
    private User assignedUser; // optional

    @DBRef
    private Tag tag; // optional

    // --- Getters & Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public User getAssignedUser() { return assignedUser; }
    public void setAssignedUser(User assignedUser) { this.assignedUser = assignedUser; }

    public Tag getTag() { return tag; }
    public void setTag(Tag tag) { this.tag = tag; }
}
