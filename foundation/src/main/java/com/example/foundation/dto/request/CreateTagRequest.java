package com.example.foundation.dto.request;

public class CreateTagRequest {
    private String tagId;
    private String tagType;

    // Constructors
    public CreateTagRequest() {}

    public CreateTagRequest(String tagId, String tagType) {
        this.tagId = tagId;
        this.tagType = tagType;
    }

    // Getters & Setters
    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }
}
