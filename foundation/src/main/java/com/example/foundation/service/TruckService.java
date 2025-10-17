package com.example.foundation.service;

import com.example.foundation.model.Truck;
import com.example.foundation.model.Route;
import com.example.foundation.repository.TruckRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TruckService {

    private final TruckRepository truckRepository;

    public TruckService(TruckRepository truckRepository) {
        this.truckRepository = truckRepository;
    }

    // 🔹 Get all trucks
    public List<Truck> getAllTrucks() {
        return truckRepository.findAll();
    }

    // 🔹 Get truck by ID
    public Optional<Truck> getTruckById(String id) {
        return truckRepository.findById(id);
    }

    // 🔹 Create new truck
    public Truck createTruck(Truck truck) {
        return truckRepository.save(truck);
    }

    // 🔹 Update truck
    public Truck updateTruck(String id, Truck updatedTruck) {
        Truck existingTruck = truckRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Truck not found"));

        existingTruck.setRegistrationNumber(updatedTruck.getRegistrationNumber());
        existingTruck.setModel(updatedTruck.getModel());
        existingTruck.setStatus(updatedTruck.getStatus());
        existingTruck.setAssignedRoute(updatedTruck.getAssignedRoute());
        existingTruck.setAssignedCrew(updatedTruck.getAssignedCrew());

        return truckRepository.save(existingTruck);
    }

    // 🔹 Delete truck
    public void deleteTruck(String id) {
        truckRepository.deleteById(id);
    }

    // 🔹 Assign a route to a truck
    public Truck assignRoute(String truckId, Route route) {
        Truck truck = truckRepository.findById(truckId)
                .orElseThrow(() -> new RuntimeException("Truck not found"));
        truck.setAssignedRoute(route);
        truck.setStatus("Assigned");
        return truckRepository.save(truck);
    }

    // 🔹 Unassign a route
    public Truck unassignRoute(String truckId) {
        Truck truck = truckRepository.findById(truckId)
                .orElseThrow(() -> new RuntimeException("Truck not found"));
        truck.setAssignedRoute(null);
        truck.setStatus("Available");
        return truckRepository.save(truck);
    }

    // 🔹 Get trucks by route ID (custom finder)
    public List<Truck> getTrucksByRoute(String routeId) {
        return truckRepository.findByAssignedRoute_Id(routeId);
    }
}
