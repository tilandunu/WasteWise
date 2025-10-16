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
    private Zone zone; // ✅ replaced String zone → Zone reference

    @DBRef
    private List<Premises> premisesList = new ArrayList<>(); // premises covered by this route

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

    public List<Premises> getPremisesList() {
        return premisesList;
    }

    public void setPremisesList(List<Premises> premisesList) {
        this.premisesList = premisesList;
    }
}
