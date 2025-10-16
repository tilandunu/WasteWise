package com.example.foundation.controller;

import com.example.foundation.model.Truck;
import com.example.foundation.service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trucks")
public class TruckController {

    @Autowired
    private TruckService truckService;

    @GetMapping("/getAll")
    public List<Truck> getAllTrucks() {
        return truckService.getAllTrucks();
    }

    @GetMapping("/{id}")
    public Truck getTruckById(@PathVariable String id) {
        return truckService.getTruckById(id).orElseThrow(() -> new RuntimeException("Truck not found"));
    }

    @PostMapping
    public Truck createTruck(@RequestBody Truck truck) {
        return truckService.createTruck(truck);
    }

    @PutMapping("/{id}")
    public Truck updateTruck(@PathVariable String id, @RequestBody Truck truck) {
        return truckService.updateTruck(id, truck);
    }

    @DeleteMapping("/{id}")
    public void deleteTruck(@PathVariable String id) {
        truckService.deleteTruck(id);
    }

    // Assign crew to truck
    @PostMapping("/{truckId}/assign-crew/{crewId}")
    public Truck assignCrewToTruck(@PathVariable String truckId, @PathVariable String crewId) {
        return truckService.assignCrewToTruck(truckId, crewId);
    }

    @PostMapping("/{truckId}/remove-crew/{crewId}")
    public Truck removeCrewFromTruck(@PathVariable String truckId, @PathVariable String crewId) {
        return truckService.removeCrewFromTruck(truckId, crewId);
    }

    // Assign route to truck
    @PostMapping("/{truckId}/assign-route/{routeId}")
    public Truck assignRoute(@PathVariable String truckId, @PathVariable String routeId) {
        return truckService.assignRouteToTruck(truckId, routeId);
    }
}
