package com.example.foundation.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "routes")
public class Route {

    @Id
    private String id;

    private String routeName;
    private boolean active = true;

    @DBRef
    private Zone zone; // Each route belongs to a Zone

    @DBRef
    private List<Truck> trucks = new ArrayList<>(); // Trucks assigned to this route

    // --- Constructors ---
    public Route() {}

    public Route(String routeName, Zone zone, boolean active) {
        this.routeName = routeName;
        this.zone = zone;
        this.active = active;
    }

    // --- Getters & Setters ---
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public List<Truck> getTrucks() {
        return trucks;
    }

    public void setTrucks(List<Truck> trucks) {
        this.trucks = trucks;
    }

    // Optional helper methods
    public void addTruck(Truck truck) {
        if (!this.trucks.contains(truck)) {
            this.trucks.add(truck);
        }
    }

    public void removeTruck(Truck truck) {
        this.trucks.remove(truck);
    }
}
