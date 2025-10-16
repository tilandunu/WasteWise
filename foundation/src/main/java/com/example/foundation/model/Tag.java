package com.example.foundation.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tags")
public class Tag {

    @Id
    private String id;
    private String tagId; // e.g., QR code or unique identifier
    private boolean active = true; // initially active

    // --- Getters & Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTagId() { return tagId; }
    public void setTagId(String tagId) { this.tagId = tagId; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
