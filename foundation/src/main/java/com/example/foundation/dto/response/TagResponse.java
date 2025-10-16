package com.example.foundation.dto.response;

public class TagResponse {
    private String id;
    private String tagId;
    private String tagType;
    private boolean active;

    public TagResponse(String id, String tagId, String tagType, boolean active) {
        this.id = id;
        this.tagId = tagId;
        this.tagType = tagType;
        this.active = active;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTagId() {
        return tagId;
    }

    public String getTagType() {
        return tagType;
    }

    public boolean isActive() {
        return active;
    }
}
