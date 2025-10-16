package com.example.foundation.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "routes")
public class Route {

    @Id
    private String id;

    private String routeName;
    private String zone;
    private boolean active;

    @DBRef
    private Truck assignedTruck; // The truck responsible for this route

    @DBRef
    private List<Premises> premisesList; // All premises under this route

    // Constructors
    public Route() {}

    public Route(String routeName, String zone, boolean active) {
        this.routeName = routeName;
        this.zone = zone;
        this.active = active;
    }

    // Getters & Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Truck getAssignedTruck() {
        return assignedTruck;
    }

    public void setAssignedTruck(Truck assignedTruck) {
        this.assignedTruck = assignedTruck;
    }

    public List<Premises> getPremisesList() {
        return premisesList;
    }

    public void setPremisesList(List<Premises> premisesList) {
        this.premisesList = premisesList;
    }
}
