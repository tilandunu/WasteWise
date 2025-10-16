package com.example.foundation.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tags")
public class Tag {

    @Id
    private String id;

    private String tagId; // e.g., QR code or RFID unique ID
    private String tagType; // e.g., "QR_CODE", "RFID", "SENSOR"
    private boolean active = true;

    // Constructors
    public Tag() {}
    public Tag(String tagId, String tagType) {
        this.tagId = tagId;
        this.tagType = tagType;
    }
    public boolean isActive() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isActive'");
    }
    public void setActive(boolean b) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setActive'");
    }
    public String getTagId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTagId'");
    }

    // Getters & Setters
    // ...
}
