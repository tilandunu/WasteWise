package com.example.foundation.controller;

import com.example.foundation.model.Route;
import com.example.foundation.service.RouteService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    // --- Get all routes ---
    @GetMapping("getAll")
    public ResponseEntity<List<Route>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    // --- Get a single route ---
    @GetMapping("/{id}")
    public ResponseEntity<?> getRouteById(@PathVariable String id) {
        return routeService.getRouteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- Create route ---
    @PostMapping("create")
    public ResponseEntity<Route> createRoute(@RequestBody Route route) {
        Route createdRoute = routeService.createRoute(route);
        return ResponseEntity.ok(createdRoute);
    }

    // --- Update route ---
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoute(@PathVariable String id, @RequestBody Route updatedRoute) {
        try {
            Route route = routeService.updateRoute(id, updatedRoute);
            return ResponseEntity.ok(route);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Delete route ---
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoute(@PathVariable String id) {
        try {
            routeService.deleteRoute(id);
            return ResponseEntity.ok("Route deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Assign truck to route ---
    @PutMapping("/{routeId}/assign-truck/{truckId}")
    public ResponseEntity<?> assignTruck(@PathVariable String routeId, @PathVariable String truckId) {
        Route updatedRoute = routeService.assignTruck(routeId, truckId);
        return ResponseEntity.ok(updatedRoute);
    }

    // --- Unassign truck from route ---
    @PutMapping("/{routeId}/unassign-truck/{truckId}")
    public ResponseEntity<?> unassignTruck(@PathVariable String routeId, @PathVariable String truckId) {
        Route updatedRoute = routeService.unassignTruck(routeId, truckId);
        return ResponseEntity.ok(updatedRoute);
    }

    // --- Get active routes ---
    @GetMapping("/active")
    public ResponseEntity<List<Route>> getActiveRoutes() {
        return ResponseEntity.ok(routeService.getActiveRoutes());
    }
}
