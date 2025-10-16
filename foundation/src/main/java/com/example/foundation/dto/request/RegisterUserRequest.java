package com.example.foundation.dto.request;

import com.example.foundation.model.Zone;

public class RegisterUserRequest {
    private String username;
    private String password;
    private String address;
    private String contactNumber;
    private Zone zone;

    // --- Getters & Setters ---
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public Zone getZone() { return zone; }
    public void setZone(Zone zone) { this.zone = zone; }
}
