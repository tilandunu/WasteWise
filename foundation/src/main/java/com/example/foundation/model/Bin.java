package com.example.foundation.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bins")
public class Bin {

    @Id
    private String id;

    private String binCode; // internal ID or serial number
    private String type; // e.g., "General", "Recyclable"
    private String status = "IN_STOCK"; // "IN_STOCK", "ASSIGNED", "DAMAGED"

    @DBRef
    private Tag tag; // Can be null initially

    @DBRef
    private Premises premises; // Can also be null initially

    // --- Constructors ---
    public Bin() {}

    public Bin(String binCode, String type) {
        this.binCode = binCode;
        this.type = type;
        this.status = "IN_STOCK";
        this.tag = null; // initially no tag
        this.premises = null; // initially not assigned
    }

    // --- Getters ---
    public String getId() {
        return id;
    }

    public String getBinCode() {
        return binCode;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public Tag getTagId() {
        return tag;
    }

    public Premises getPremises() {
        return premises;
    }

    // --- Setters ---
    public void setId(String id) {
        this.id = id;
    }

    public void setBinCode(String binCode) {
        this.binCode = binCode;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public void setPremises(Premises premises) {
        this.premises = premises;
    }

    @Override
    public String toString() {
        return "Bin{" +
                "id='" + id + '\'' +
                ", binCode='" + binCode + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", tag=" + (tag != null ? tag.getId() : "null") +
                ", premises=" + (premises != null ? premises.getId() : "null") +
                '}';
    }
}
