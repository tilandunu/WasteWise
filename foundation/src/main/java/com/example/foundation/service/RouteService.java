package com.example.foundation.service;

import com.example.foundation.model.Route;
import com.example.foundation.model.Truck;
import com.example.foundation.model.Zone;
import com.example.foundation.repository.RouteRepository;
import com.example.foundation.repository.TruckRepository;
import com.example.foundation.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private TruckRepository truckRepository;

    // --- CRUD Operations ---
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    public Optional<Route> getRouteById(String id) {
        return routeRepository.findById(id);
    }

    public Route createRoute(Route route) {
        return routeRepository.save(route);
    }

    public Route updateRoute(String id, Route updatedRoute) {
        return routeRepository.findById(id).map(route -> {
            route.setRouteName(updatedRoute.getRouteName());
            route.setActive(updatedRoute.isActive());
            route.setZone(updatedRoute.getZone());
            route.setTrucks(updatedRoute.getTrucks());
            return routeRepository.save(route);
        }).orElseThrow(() -> new RuntimeException("Route not found"));
    }

    public void deleteRoute(String id) {
        routeRepository.deleteById(id);
    }

    // --- Assign Zone to Route ---
    public Route assignZoneToRoute(String routeId, String zoneId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        route.setZone(zone);
        return routeRepository.save(route);
    }

    // --- Assign Truck to Route ---
    public Route assignTruckToRoute(String routeId, String truckId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        Truck truck = truckRepository.findById(truckId)
                .orElseThrow(() -> new RuntimeException("Truck not found"));

        if (!route.getTrucks().contains(truck)) {
            route.getTrucks().add(truck);
            truck.setAssignedRoute(route);
            truckRepository.save(truck);
        }

        return routeRepository.save(route);
    }

    // --- Query Methods ---
    public List<Route> getActiveRoutes() {
        return routeRepository.findByActiveTrue();
    }

    public List<Route> getRoutesByZone(String zoneId) {
        return routeRepository.findByZone_Id(zoneId);
    }

    public Route getRouteByName(String name) {
        return routeRepository.findByRouteName(name);
    }
}
