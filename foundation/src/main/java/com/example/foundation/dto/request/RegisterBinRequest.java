package com.example.foundation.dto.request;

public class RegisterBinRequest {
    private String binCode;
    private String type;       // "General" or "Recyclable"
    private String tagId;      // Optional
    private String premisesId; // Optional

    // Getters and setters
    public String getBinCode() { return binCode; }
    public void setBinCode(String binCode) { this.binCode = binCode; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTagId() { return tagId; }
    public void setTagId(String tagId) { this.tagId = tagId; }

    public String getPremisesId() { return premisesId; }
    public void setPremisesId(String premisesId) { this.premisesId = premisesId; }
}
