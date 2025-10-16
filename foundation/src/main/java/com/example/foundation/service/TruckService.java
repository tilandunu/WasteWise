package com.example.foundation.service;

import com.example.foundation.model.Truck;
import com.example.foundation.model.User;
import com.example.foundation.model.Route;
import com.example.foundation.repository.TruckRepository;
import com.example.foundation.repository.UserRepository;
import com.example.foundation.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TruckService {

    @Autowired
    private TruckRepository truckRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RouteRepository routeRepository;

    // --- CRUD Operations ---
    public List<Truck> getAllTrucks() {
        return truckRepository.findAll();
    }

    public Optional<Truck> getTruckById(String id) {
        return truckRepository.findById(id);
    }

    public Truck createTruck(Truck truck) {
        return truckRepository.save(truck);
    }

    public Truck updateTruck(String id, Truck updatedTruck) {
        return truckRepository.findById(id).map(truck -> {
            truck.setRegistrationNumber(updatedTruck.getRegistrationNumber());
            truck.setModel(updatedTruck.getModel());
            truck.setStatus(updatedTruck.getStatus());
            truck.setRoute(updatedTruck.getRoute());
            truck.setAssignedUsers(updatedTruck.getAssignedUsers());
            return truckRepository.save(truck);
        }).orElseThrow(() -> new RuntimeException("Truck not found"));
    }

    public void deleteTruck(String id) {
        truckRepository.deleteById(id);
    }

    // --- Business Logic ---
    public Truck assignUserToTruck(String truckId, String userId) {
        Truck truck = truckRepository.findById(truckId)
                .orElseThrow(() -> new RuntimeException("Truck not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!truck.getAssignedUsers().contains(user)) {
            truck.getAssignedUsers().add(user);
        }

        return truckRepository.save(truck);
    }

    public Truck removeUserFromTruck(String truckId, String userId) {
        Truck truck = truckRepository.findById(truckId)
                .orElseThrow(() -> new RuntimeException("Truck not found"));

        truck.getAssignedUsers().removeIf(u -> u.getId().equals(userId));
        return truckRepository.save(truck);
    }

    public Truck assignRouteToTruck(String truckId, String routeId) {
        Truck truck = truckRepository.findById(truckId)
                .orElseThrow(() -> new RuntimeException("Truck not found"));
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        truck.setRoute(route);
        return truckRepository.save(truck);
    }

    // --- Query Methods ---
    public List<Truck> getTrucksByStatus(String status) {
        return truckRepository.findByStatus(status);
    }

    public List<Truck> getTrucksByRoute(String routeId) {
        return truckRepository.findByRoute_Id(routeId);
    }

    public List<Truck> getTrucksByUser(String userId) {
        return truckRepository.findByAssignedUsers_Id(userId);
    }
}
