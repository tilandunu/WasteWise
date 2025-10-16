package com.example.foundation.dto.request;

public class RegisterUserRequest {
    private String username;
    private String password;
    private String address;
    private String contactNumber;
    private String zoneId;           // Zone reference
    private String premisesType;     // NEW field

    // --- Getters & Setters ---
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getZoneId() { return zoneId; }
    public void setZoneId(String zoneId) { this.zoneId = zoneId; }

    public String getPremisesType() { return premisesType; }
    public void setPremisesType(String premisesType) { this.premisesType = premisesType; }
}
