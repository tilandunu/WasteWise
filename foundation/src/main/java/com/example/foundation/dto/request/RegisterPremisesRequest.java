package com.example.foundation.dto.request;

public class RegisterPremisesRequest {

    private String address;
    private String type; // "Household" or "Business"
    private String contactNumber;
    private String zoneId;
    private String ownerUid; // from Firebase user
    private String binId; // e.g., "General", "Recyclable"
    private String tagId; // scanned or manually entered tag

    // Getters & Setters
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(String ownerUid) {
        this.ownerUid = ownerUid;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getBinCode() {
        return binId;
    }
}
