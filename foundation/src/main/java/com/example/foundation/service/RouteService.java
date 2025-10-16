package com.example.foundation.service;

import com.example.foundation.model.Route;
import com.example.foundation.model.Zone;
import com.example.foundation.repository.RouteRepository;
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
            route.setPremisesList(updatedRoute.getPremisesList());
            return routeRepository.save(route);
        }).orElseThrow(() -> new RuntimeException("Route not found"));
    }

    public void deleteRoute(String id) {
        routeRepository.deleteById(id);
    }

    // --- Business Logic ---
    public Route assignZoneToRoute(String routeId, String zoneId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        route.setZone(zone);
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
