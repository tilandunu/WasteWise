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
    private String status = "IN_STOCK"; // or "ASSIGNED", "DAMAGED"

    @DBRef
    private Tag tag;

    @DBRef
    private Premises premises;

    // Constructors
    public Bin() {}

    public Bin(String binCode, String type, Tag tag, Premises premises) {
        this.binCode = binCode;
        this.type = type;
        this.tag = tag;
        this.premises = premises;
    }

    public String getType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getType'");
    }

    public void setStatus(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setStatus'");
    }

    public void setPremises(Premises premises2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPremises'");
    }

    public void setTag(Tag tag2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setTag'");
    }

    public String getId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getId'");
    }

    public String getStatus() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStatus'");
    }

    // Getters & Setters
    // ...
}
