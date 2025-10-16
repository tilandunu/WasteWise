package com.example.foundation.service;

import com.example.foundation.dto.request.CreateTagRequest;
import com.example.foundation.dto.response.TagResponse;
import com.example.foundation.model.Tag;
import com.example.foundation.repository.TagRepository;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagResponse createTag(CreateTagRequest request) {
        // Prevent duplicates
        tagRepository.findByTagId(request.getTagId()).ifPresent(existing -> {
            throw new RuntimeException("Tag with this ID already exists");
        });

        Tag tag = new Tag();
        tag.setTagId(request.getTagId());
        tag.setTagType(request.getTagType());
        tag.setActive(true);

        Tag savedTag = tagRepository.save(tag);

        return new TagResponse(
                savedTag.getId(),
                savedTag.getTagId(),
                savedTag.getTagType(),
                savedTag.isActive()
        );
    }
}
