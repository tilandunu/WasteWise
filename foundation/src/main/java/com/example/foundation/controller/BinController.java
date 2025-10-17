package com.example.foundation.controller;

import com.example.foundation.model.Bin;
import com.example.foundation.model.Tag;
import com.example.foundation.service.BinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bins")
public class BinController {

    // Inject the BinService
    private final BinService binService;

    // Constructor
    public BinController(BinService binService) {
        this.binService = binService;
    }

    // --- Get all unassigned bins ---
    @GetMapping("/unassigned")
    public ResponseEntity<List<Bin>> getUnassignedBins() {
        List<Bin> bins = binService.getUnassignedBins();
        return ResponseEntity.ok(bins);
    }

    // --- Get all assigned bins ---
    @GetMapping("/assigned")
    public ResponseEntity<List<Bin>> getAssignedBins() {
        List<Bin> bins = binService.getAssignedBins();
        return ResponseEntity.ok(bins);
    }

    //Scan bin 
    @GetMapping("/scan/{tagId}")
    public ResponseEntity<Tag> scanBin(@PathVariable String tagId) {
        Tag scannedBin = binService.scanBin(tagId);
        return ResponseEntity.ok(scannedBin);
    }

    
}
