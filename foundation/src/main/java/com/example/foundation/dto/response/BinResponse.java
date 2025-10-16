package com.example.foundation.dto.response;

public class BinResponse {
    private String id;
    private String binCode;

    public BinResponse(String id, String binCode) {
        this.id = id;
        this.binCode = binCode;
    }

    // Getters
    public String getId() { return id; }
    public String getBinCode() { return binCode; }
}
