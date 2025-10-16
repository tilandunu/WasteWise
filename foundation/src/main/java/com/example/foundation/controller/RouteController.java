package com.example.foundation.controller;

import com.example.foundation.model.Route;
import com.example.foundation.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping
    public List<Route> getAllRoutes() {
        return routeService.getAllRoutes();
    }

    @GetMapping("/{id}")
    public Route getRouteById(@PathVariable String id) {
        return routeService.getRouteById(id).orElseThrow(() -> new RuntimeException("Route not found"));
    }

    @PostMapping
    public Route createRoute(@RequestBody Route route) {
        return routeService.createRoute(route);
    }

    @PutMapping("/{id}")
    public Route updateRoute(@PathVariable String id, @RequestBody Route route) {
        return routeService.updateRoute(id, route);
    }

    @DeleteMapping("/{id}")
    public void deleteRoute(@PathVariable String id) {
        routeService.deleteRoute(id);
    }

    // Assign zone to route
    @PostMapping("/{routeId}/assign-zone/{zoneId}")
    public Route assignZone(@PathVariable String routeId, @PathVariable String zoneId) {
        return routeService.assignZoneToRoute(routeId, zoneId);
    }

    // Get active routes
    @GetMapping("/active")
    public List<Route> getActiveRoutes() {
        return routeService.getActiveRoutes();
    }

    // Get routes by zone
    @GetMapping("/zone/{zoneId}")
    public List<Route> getRoutesByZone(@PathVariable String zoneId) {
        return routeService.getRoutesByZone(zoneId);
    }
}
