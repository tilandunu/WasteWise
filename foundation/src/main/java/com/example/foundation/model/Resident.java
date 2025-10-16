package com.example.foundation.model;

public class Resident extends User {

    public enum PremisesType {
        HOUSE,
        BUSINESS
    }

    private PremisesType premisesType;

    public Resident(String username, String password, String address, String contactNumber) {
        super(username, password, address, contactNumber);
    }

    public PremisesType getPremisesType() { return premisesType; }
    public void setPremisesType(PremisesType premisesType) { this.premisesType = premisesType; }
}
