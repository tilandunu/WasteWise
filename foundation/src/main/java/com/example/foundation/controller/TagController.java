package com.example.foundation.controller;

import com.example.foundation.dto.request.CreateTagRequest;
import com.example.foundation.dto.response.TagResponse;
import com.example.foundation.service.TagService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping("/create")
    public TagResponse createTag(@RequestBody CreateTagRequest request) {
        return tagService.createTag(request);
    }
}
