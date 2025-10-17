package com.example.foundation.controller;

import com.example.foundation.model.Bin;
import com.example.foundation.model.CollectionEvent;
import com.example.foundation.model.Tag;
import com.example.foundation.repository.BinRepository;
import com.example.foundation.repository.TagRepository;
import com.example.foundation.service.CollectionEventService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/collection-event")
public class CollectionEventController {

    @Autowired
    private BinRepository binRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CollectionEventService collectionEventService;

    @PostMapping()
    public ResponseEntity<?> collectBin(
            @RequestParam String binId,
            @RequestParam String tagId,
            @RequestParam String crewId) 
        {
             try {
            Optional<Bin> binOpt = binRepository.findById(binId);
            if (binOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Bin not found");
            }

            Optional<Tag> tagOpt = tagRepository.findByTagId(tagId);
            if (tagOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Tag not found");
            }

            Bin bin = binOpt.get();
            Tag tag = tagOpt.get();

            // Create collection event
            CollectionEvent event = collectionEventService.createCollectionEvent(bin, tag.getWeight());

            if (event != null) {
                tag.setWeight(0);
                tag.setFillLevel(0);
                tagRepository.save(tag);
                return ResponseEntity.ok().body("Bin " + bin.getBinCode() + " marked as collected");
            } else {
                return ResponseEntity.status(500).body("Failed to collect bin");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to collect bin");
        }
    }
}
