package com.example.foundation.dto.response;

public class PremisesResponse {

    private String premisesId;
    private String address;
    private String type;
    private String contactNumber;
    private String zoneName;
    private String ownerName;
    private BinResponse bin;

    // --- Inner DTO for bin details ---
    public static class BinResponse {
        private String binId;
        private String binType;
        private String status;
        private String tagId;

        // --- Getters & Setters ---
        public String getBinId() {
            return binId;
        }

        public void setBinId(String binId) {
            this.binId = binId;
        }

        public String getBinType() {
            return binType;
        }

        public void setBinType(String binType) {
            this.binType = binType;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTagId() {
            return tagId;
        }

        public void setTagId(String tagId) {
            this.tagId = tagId;
        }
    }

    // --- Getters ---
    public String getPremisesId() {
        return premisesId;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getZoneName() {
        return zoneName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public BinResponse getBin() {
        return bin;
    }

    // --- Setters ---
    public void setPremisesId(String premisesId) {
        this.premisesId = premisesId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setBin(BinResponse bin) {
        this.bin = bin;
    }

    // --- toString ---
    @Override
    public String toString() {
        return "PremisesResponse{" +
                "premisesId='" + premisesId + '\'' +
                ", address='" + address + '\'' +
                ", type='" + type + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", zoneName='" + zoneName + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", bin=" + (bin != null ? bin.getBinId() : "null") +
                '}';
    }
}
