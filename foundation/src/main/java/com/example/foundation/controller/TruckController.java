package com.example.foundation.controller;

import com.example.foundation.model.Truck;
import com.example.foundation.model.Route;
import com.example.foundation.service.TruckService;
import com.example.foundation.repository.RouteRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trucks")
public class TruckController {

    private final TruckService truckService;
    private final RouteRepository routeRepository;

    public TruckController(TruckService truckService, RouteRepository routeRepository) {
        this.truckService = truckService;
        this.routeRepository = routeRepository;
    }

    // 🔹 Get all trucks
    @GetMapping("/getAll")
    public ResponseEntity<List<Truck>> getAllTrucks() {
        return ResponseEntity.ok(truckService.getAllTrucks());
    }

    // 🔹 Get a single truck by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getTruckById(@PathVariable String id) {
        return truckService.getTruckById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Create a new truck
    @PostMapping("/create")
    public ResponseEntity<Truck> createTruck(@RequestBody Truck truck) {
        Truck savedTruck = truckService.createTruck(truck);
        return ResponseEntity.ok(savedTruck);
    }

    // 🔹 Update truck details
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTruck(@PathVariable String id, @RequestBody Truck updatedTruck) {
        try {
            Truck truck = truckService.updateTruck(id, updatedTruck);
            return ResponseEntity.ok(truck);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 🔹 Delete a truck
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTruck(@PathVariable String id) {
        try {
            truckService.deleteTruck(id);
            return ResponseEntity.ok("Truck deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 🔹 Assign a route to a truck
    @PutMapping("/{truckId}/assign-route/{routeId}")
    public ResponseEntity<?> assignRoute(@PathVariable String truckId, @PathVariable String routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));
        Truck updatedTruck = truckService.assignRoute(truckId, route);
        return ResponseEntity.ok(updatedTruck);
    }

    // 🔹 Unassign route from truck
    @PutMapping("/{truckId}/unassign-route")
    public ResponseEntity<?> unassignRoute(@PathVariable String truckId) {
        Truck updatedTruck = truckService.unassignRoute(truckId);
        return ResponseEntity.ok(updatedTruck);
    }

    // 🔹 Get all trucks assigned to a specific route
    @GetMapping("/by-route/{routeId}")
    public ResponseEntity<List<Truck>> getTrucksByRoute(@PathVariable String routeId) {
        List<Truck> trucks = truckService.getTrucksByRoute(routeId);
        return ResponseEntity.ok(trucks);
    }
}
