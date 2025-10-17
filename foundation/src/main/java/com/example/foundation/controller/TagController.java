package com.example.foundation.controller;

import com.example.foundation.model.Tag;
import com.example.foundation.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    // Inject the TagService
    private final TagService tagService;


    // Constructor
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    // --- Get all tags ---
    @GetMapping("/all")
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    //Scan tag by tagId
    @GetMapping("/scan/{tagId}")
    public ResponseEntity<Tag> scanTag(@PathVariable String tagId) {
        Tag scannedTag = tagService.scanTag(tagId);
        return ResponseEntity.ok(scannedTag);
    }
}
