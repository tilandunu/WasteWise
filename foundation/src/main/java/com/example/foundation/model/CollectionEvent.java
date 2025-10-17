package com.example.foundation.model;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "collection-events")
public class CollectionEvent {

    private float weight;

    @DBRef
    private Bin assignedBin;

    private Date collectedDate; // new field

    public CollectionEvent(Bin assignedBin, float weight) {
        this.assignedBin = assignedBin;
        this.weight = weight;
        this.collectedDate = new Date(); // set to current time by default
    }

    // --- Getters & Setters ---
    public float getWeight() { return weight; }
    public void setWeight(float weight) { this.weight = weight; }

    public Bin getAssignedBin() { return assignedBin; }
    public void setAssignedBin(Bin assignedBin) { this.assignedBin = assignedBin; }

    public Date getCollectedDate() { return collectedDate; }
    public void setCollectedDate(Date collectedDate) { this.collectedDate = collectedDate; }
}
