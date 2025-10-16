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

    public void setAddress(String address2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setAddress'");
    }

    public void setType(String type2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setType'");
    }

    public void setContactNumber(String contactNumber2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setContactNumber'");
    }

    public void setOwner(User owner2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setOwner'");
    }

    public void setZone(Zone zone2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setZone'");
    }

    public Object getId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getId'");
    }

    public Object getAddress() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAddress'");
    }

    public Object getType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getType'");
    }

    public Object getContactNumber() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getContactNumber'");
    }

    // Getters & Setters
    // ...
}
