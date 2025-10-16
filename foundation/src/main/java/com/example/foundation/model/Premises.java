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
    private boolean eligible = true;

    @DBRef
    private User owner;

    @DBRef
    private Zone zone;

    @DBRef
    private Route route; // Each premises belongs to one route

    // Constructors
    public Premises() {}

    public Premises(String address, String type, String contactNumber, User owner, Zone zone, Route route) {
        this.address = address;
        this.type = type;
        this.contactNumber = contactNumber;
        this.owner = owner;
        this.zone = zone;
        this.route = route;
    }

    // Getters & Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public boolean isEligible() {
        return eligible;
    }

    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
