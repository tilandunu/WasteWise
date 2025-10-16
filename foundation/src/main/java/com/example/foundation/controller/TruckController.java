package com.example.foundation.controller;

import com.example.foundation.model.Truck;
import com.example.foundation.service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trucks")
public class TruckController {

    @Autowired
    private TruckService truckService;

    // --- CRUD Endpoints ---
    @GetMapping("/getAll")
    public List<Truck> getAllTrucks() {
        return truckService.getAllTrucks();
    }

    @GetMapping("/{id}")
    public Optional<Truck> getTruckById(@PathVariable String id) {
        return truckService.getTruckById(id);
    }

    @PostMapping("/create")
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

    // --- Assign a user (crew) to a truck ---
    @PostMapping("/{truckId}/assign-user/{userId}")
    public Truck assignUserToTruck(@PathVariable String truckId, @PathVariable String userId) {
        return truckService.assignUserToTruck(truckId, userId);
    }

    // --- Remove a user from a truck ---
    @PostMapping("/{truckId}/remove-user/{userId}")
    public Truck removeUserFromTruck(@PathVariable String truckId, @PathVariable String userId) {
        return truckService.removeUserFromTruck(truckId, userId);
    }

    // --- Assign a route to a truck ---
    @PostMapping("/{truckId}/assign-route/{routeId}")
    public Truck assignRouteToTruck(@PathVariable String truckId, @PathVariable String routeId) {
        return truckService.assignRouteToTruck(truckId, routeId);
    }

    // --- Query trucks ---
    @GetMapping("/status/{status}")
    public List<Truck> getTrucksByStatus(@PathVariable String status) {
        return truckService.getTrucksByStatus(status);
    }

    @GetMapping("/route/{routeId}")
    public List<Truck> getTrucksByRoute(@PathVariable String routeId) {
        return truckService.getTrucksByRoute(routeId);
    }

    @GetMapping("/user/{userId}")
    public List<Truck> getTrucksByUser(@PathVariable String userId) {
        return truckService.getTrucksByUser(userId);
    }
}
