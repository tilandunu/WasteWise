package com.example.foundation.controller;

import com.example.foundation.model.Bin;
import com.example.foundation.service.OfficerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/officer")
public class OfficerController {

    // Dependency injection
    private final OfficerService officerService;

    // Constructor
    public OfficerController(OfficerService officerService) {
        this.officerService = officerService;
    }

    // Assign a tag to a bin
    @PostMapping("/assign-tag")
    public ResponseEntity<Bin> assignTagToBin(@RequestParam String binId, @RequestParam String tagId) {
        Bin updatedBin = officerService.assignTagToBin(binId, tagId);
        return ResponseEntity.ok(updatedBin);
    }

    // Assign a bin to a resident
    @PostMapping("/assign-bin")
    public ResponseEntity<Bin> assignBinToResident(@RequestParam String binId, @RequestParam String residentId) {
        Bin updatedBin = officerService.assignBinToResident(binId, residentId);
        return ResponseEntity.ok(updatedBin);
    }
}
