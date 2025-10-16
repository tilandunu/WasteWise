package com.example.foundation.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "premises")
public class Premises {

    @Id
    private String id;

    private String address;
    private String type; // e.g. "Household" or "Business"
    private String contactNumber;

    @DBRef
    private User owner;

    @DBRef
    private Zone zone;

    private boolean eligible = true;

    // --- Constructors ---
    public Premises() {}

    public Premises(String address, String type, String contactNumber, User owner, Zone zone) {
        this.address = address;
        this.type = type;
        this.contactNumber = contactNumber;
        this.owner = owner;
        this.zone = zone;
        this.eligible = true;
    }

    // --- Getters ---
    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public User getOwner() {
        return owner;
    }

    public Zone getZone() {
        return zone;
    }

    public boolean isEligible() {
        return eligible;
    }

    // --- Setters ---
    public void setId(String id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }

    // --- toString ---
    @Override
    public String toString() {
        return "Premises{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", type='" + type + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", owner=" + (owner != null ? owner.getName() : "null") +
                ", zone=" + (zone != null ? zone.getName() : "null") +
                ", eligible=" + eligible +
                '}';
    }
}
