package com.example.foundation.controller;

import com.example.foundation.model.Zone;
import com.example.foundation.service.ZoneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
public class ZoneController {


    // Dependency injection
    private final ZoneService zoneService;

    // Constructor
    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    // Create a new zone
    @PostMapping("/create")
    public ResponseEntity<?> createZone(@RequestBody Zone zoneRequest) {
        try {
            Zone zone = zoneService.createZone(zoneRequest.getName());
            return ResponseEntity.ok(zone);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get all zones
    @GetMapping("/all")
    public ResponseEntity<List<Zone>> getAllZones() {
        return ResponseEntity.ok(zoneService.getAllZones());
    }

    // Get a zone by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getZoneById(@PathVariable String id) {
        try {
            Zone zone = zoneService.getZoneById(id);
            return ResponseEntity.ok(zone);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
