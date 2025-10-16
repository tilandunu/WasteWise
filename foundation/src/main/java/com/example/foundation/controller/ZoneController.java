package com.example.foundation.controller;

import com.example.foundation.dto.request.ZoneRequest;
import com.example.foundation.model.Zone;
import com.example.foundation.service.ZoneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/zones")
public class ZoneController {

    private final ZoneService zoneService;

    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createZone(@RequestBody ZoneRequest request) {
        if (request.getName() == null || request.getName().isEmpty()) {
            return ResponseEntity.badRequest().body("Zone name is required");
        }

        Zone zone = zoneService.createZone(request.getName());
        return ResponseEntity.ok(zone);
    }
}
