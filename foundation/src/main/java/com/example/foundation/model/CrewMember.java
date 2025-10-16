package com.example.foundation.model;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "crew_members")
public class CrewMember extends User {

    private boolean available = true; // whether crew member is available for assignment

    @DBRef
    private Truck assignedTruck; // truck currently assigned


    // --- Constructors ---
    public CrewMember() {
        super("", "", "", "");
    }

    public CrewMember(String username, String password, String address, String contactNumber) {
        super(username, password, address, contactNumber);
    }

    // --- Getters & Setters ---
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Truck getAssignedTruck() {
        return assignedTruck;
    }

    public void setAssignedTruck(Truck assignedTruck) {
        this.assignedTruck = assignedTruck;
    }

}
