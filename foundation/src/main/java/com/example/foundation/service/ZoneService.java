package com.example.foundation.service;

import com.example.foundation.model.Zone;
import com.example.foundation.repository.ZoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneService {

    private final ZoneRepository zoneRepository;

    public ZoneService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    // Create a new Zone
    public Zone createZone(String name) {
        if (zoneRepository.existsByName(name)) {
            throw new RuntimeException("Zone already exists");
        }

        Zone zone = new Zone();
        zone.setName(name);

        return zoneRepository.save(zone);
    }

    // Get all zones
    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }

    // Get zone by ID
    public Zone getZoneById(String id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone not found"));
    }
}
