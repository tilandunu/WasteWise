package com.example.foundation.controller;

import com.example.foundation.model.Bin;
import com.example.foundation.service.BinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bins")
public class BinController {

    private final BinService binService;

    public BinController(BinService binService) {
        this.binService = binService;
    }

    // --- Get all unassigned bins ---
    @GetMapping("/unassigned")
    public ResponseEntity<List<Bin>> getUnassignedBins() {
        List<Bin> bins = binService.getUnassignedBins();
        return ResponseEntity.ok(bins);
    }

    
}
