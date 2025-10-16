package com.example.foundation.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tags")
public class Tag {

    @Id
    private String id;

    private String tagId;   // e.g., QR code or RFID unique ID
    private String tagType; // e.g., "QR_CODE", "RFID", "SENSOR"
    private boolean active = true;

    // Constructors
    public Tag() {}

    public Tag(String tagId, String tagType) {
        this.tagId = tagId;
        this.tagType = tagType;
        this.active = true;
    }

    // --- Getters ---
    public String getId() {
        return id;
    }

    public String getTagId() {
        return tagId;
    }

    public String getTagType() {
        return tagType;
    }

    public boolean isActive() {
        return active;
    }

    // --- Setters ---
    public void setId(String id) {
        this.id = id;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
