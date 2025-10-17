package com.example.foundation.service;

import com.example.foundation.model.Tag;
import com.example.foundation.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    // Get all tags
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    //Scan tag by tagId
    public Tag scanTag(String tagId) {
        return tagRepository.findByTagId(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
    }
}
