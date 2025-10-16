package com.example.foundation.service;

import com.example.foundation.model.Zone;
import com.example.foundation.repository.ZoneRepository;
import org.springframework.stereotype.Service;

@Service
public class ZoneService {

    // Dependencies
    private final ZoneRepository zoneRepository;


    // Constructor
    public ZoneService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    // Create a new zone
    public Zone createZone(String name) {
        Zone zone = new Zone(name);
        return zoneRepository.save(zone);
    }

    // Get all zones
    public Iterable<Zone> getAllZones() {
        return zoneRepository.findAll();
    }
}
