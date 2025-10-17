package com.example.foundation.service;

import com.example.foundation.model.Route;
import com.example.foundation.model.Zone;
import com.example.foundation.model.Truck;
import com.example.foundation.repository.RouteRepository;
import com.example.foundation.repository.ZoneRepository;
import com.example.foundation.repository.TruckRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RouteService {

    private final RouteRepository routeRepository;
    private final ZoneRepository zoneRepository;
    private final TruckRepository truckRepository;

    public RouteService(RouteRepository routeRepository, ZoneRepository zoneRepository, TruckRepository truckRepository) {
        this.routeRepository = routeRepository;
        this.zoneRepository = zoneRepository;
        this.truckRepository = truckRepository;
    }

    // --- Get all routes ---
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    // --- Get route by ID ---
    public Optional<Route> getRouteById(String id) {
        return routeRepository.findById(id);
    }

    // --- Create route ---
    public Route createRoute(Route route) {
        if (route.getZone() != null) {
            Zone zone = zoneRepository.findById(route.getZone().getId())
                    .orElseThrow(() -> new RuntimeException("Zone not found"));
            route.setZone(zone);
        }
        return routeRepository.save(route);
    }

    // --- Update route ---
    public Route updateRoute(String id, Route updatedRoute) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        route.setRouteName(updatedRoute.getRouteName());
        route.setActive(updatedRoute.isActive());

        if (updatedRoute.getZone() != null) {
            Zone zone = zoneRepository.findById(updatedRoute.getZone().getId())
                    .orElseThrow(() -> new RuntimeException("Zone not found"));
            route.setZone(zone);
        }

        return routeRepository.save(route);
    }

    // --- Delete route ---
    public void deleteRoute(String id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route not found"));
        routeRepository.delete(route);
    }

    // --- Assign truck to route ---
    public Route assignTruck(String routeId, String truckId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));
        Truck truck = truckRepository.findById(truckId)
                .orElseThrow(() -> new RuntimeException("Truck not found"));

        route.addTruck(truck);
        truck.setAssignedRoute(route);
        truckRepository.save(truck);

        return routeRepository.save(route);
    }

    // --- Remove truck from route ---
    public Route unassignTruck(String routeId, String truckId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));
        Truck truck = truckRepository.findById(truckId)
                .orElseThrow(() -> new RuntimeException("Truck not found"));

        route.removeTruck(truck);
        truck.setAssignedRoute(null);
        truckRepository.save(truck);

        return routeRepository.save(route);
    }

    // --- Get all active routes ---
    public List<Route> getActiveRoutes() {
        return routeRepository.findAll().stream()
                .filter(Route::isActive)
                .toList();
    }
}
