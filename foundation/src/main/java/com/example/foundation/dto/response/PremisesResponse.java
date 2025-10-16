package com.example.foundation.dto.response;

public class PremisesResponse {

    private String premisesId;
    private String address;
    private String type;
    private String contactNumber;
    private String zoneName;
    private String ownerName;
    private BinResponse bin;

    // Inner DTO for the bin details
    public static class BinResponse {
        private String binId;
        private String binType;
        private String status;
        private String tagId;

        // Getters & Setters
        public String getBinId() { return binId; }
        public void setBinId(String binId) { this.binId = binId; }

        public String getBinType() { return binType; }
        public void setBinType(String binType) { this.binType = binType; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getTagId() { return tagId; }
        public void setTagId(String tagId) { this.tagId = tagId; }
    }

    public void setPremisesId(Object id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPremisesId'");
    }

    public void setAddress(Object address2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setAddress'");
    }

    public void setType(Object type2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setType'");
    }

    public void setContactNumber(Object contactNumber2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setContactNumber'");
    }

    public void setZoneName(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setZoneName'");
    }

    public void setOwnerName(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setOwnerName'");
    }

    public void setBin(BinResponse binResponse) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setBin'");
    }

    // Getters & Setters
}
