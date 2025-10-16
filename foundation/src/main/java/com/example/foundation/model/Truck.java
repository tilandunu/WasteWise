package com.example.foundation.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "trucks")
public class Truck {

    @Id
    private String id;

    private String registrationNumber;
    private String model;
    private String status = "Available"; // Available, Assigned, UnderMaintenance

    @DBRef
    private Route assignedRoute; // Truck is assigned to one route

    @DBRef
    private List<CrewMember> assignedCrew = new ArrayList<>(); // Crew members operating this truck

    // --- Constructors ---
    public Truck() {}

    public Truck(String registrationNumber, String model, String status) {
        this.registrationNumber = registrationNumber;
        this.model = model;
        this.status = status;
    }

    // --- Getters & Setters ---
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Route getAssignedRoute() {
        return assignedRoute;
    }

    public void setAssignedRoute(Route assignedRoute) {
        this.assignedRoute = assignedRoute;
    }

    public List<CrewMember> getAssignedCrew() {
        return assignedCrew;
    }

    public void setAssignedCrew(List<CrewMember> assignedCrew) {
        this.assignedCrew = assignedCrew;
    }

    // --- Helper Methods ---
    public void addCrewMember(CrewMember crewMember) {
        if (!assignedCrew.contains(crewMember)) {
            assignedCrew.add(crewMember);
        }
    }

    public void removeCrewMember(CrewMember crewMember) {
        assignedCrew.remove(crewMember);
    }
}
