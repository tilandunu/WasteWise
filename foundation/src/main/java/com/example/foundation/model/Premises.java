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

    // Constructors
    public Premises() {}

    public Premises(String address, String type, String contactNumber, User owner, Zone zone) {
        this.address = address;
        this.type = type;
        this.contactNumber = contactNumber;
        this.owner = owner;
        this.zone = zone;
    }

    // Getters & Setters
    // ...
}
