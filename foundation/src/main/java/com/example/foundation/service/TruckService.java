package com.example.foundation.service;

import com.example.foundation.model.CrewMember;
import com.example.foundation.model.Route;
import com.example.foundation.model.Truck;
import com.example.foundation.repository.CrewMemberRepository;
import com.example.foundation.repository.RouteRepository;
import com.example.foundation.repository.TruckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TruckService {

    @Autowired
    private TruckRepository truckRepository;

    @Autowired
    private CrewMemberRepository crewRepository;

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
            truck.setAssignedRoute(updatedTruck.getAssignedRoute());
            truck.setAssignedCrew(updatedTruck.getAssignedCrew());
            return truckRepository.save(truck);
        }).orElseThrow(() -> new RuntimeException("Truck not found"));
    }

    public void deleteTruck(String id) {
        truckRepository.deleteById(id);
    }

    // --- Assign Crew to Truck ---
    public Truck assignCrewToTruck(String truckId, String crewId) {
        Truck truck = truckRepository.findById(truckId)
                .orElseThrow(() -> new RuntimeException("Truck not found"));

        CrewMember crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("Crew member not found"));

        if (!truck.getAssignedCrew().contains(crew)) {
            truck.getAssignedCrew().add(crew);
            crew.setAssignedTruck(truck);
            crewRepository.save(crew);
        }

        return truckRepository.save(truck);
    }

    public Truck removeCrewFromTruck(String truckId, String crewId) {
        Truck truck = truckRepository.findById(truckId)
                .orElseThrow(() -> new RuntimeException("Truck not found"));

        truck.getAssignedCrew().removeIf(c -> c.getId().equals(crewId));

        crewRepository.findById(crewId).ifPresent(crew -> {
            crew.setAssignedTruck(null);
            crewRepository.save(crew);
        });

        return truckRepository.save(truck);
    }

    // --- Assign Route to Truck ---
    public Truck assignRouteToTruck(String truckId, String routeId) {
        Truck truck = truckRepository.findById(truckId)
                .orElseThrow(() -> new RuntimeException("Truck not found"));

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        truck.setAssignedRoute(route);
        return truckRepository.save(truck);
    }

    // --- Query Methods ---
    public List<Truck> getTrucksByStatus(String status) {
        return truckRepository.findByStatus(status);
    }

    public List<Truck> getTrucksByRoute(String routeId) {
        return truckRepository.findByAssignedRoute_Id(routeId); // Changed from findByRoute_Id
    }

    public List<Truck> getTrucksByCrew(String crewId) {
        return truckRepository.findByAssignedCrew_Id(crewId);
    }
}
