package com.example.foundation.service;

import com.example.foundation.model.CrewMember;
import com.example.foundation.model.Truck;
import com.example.foundation.repository.CrewMemberRepository;
import com.example.foundation.repository.TruckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CrewMemberService {

    @Autowired
    private CrewMemberRepository crewRepository;

    @Autowired
    private TruckRepository truckRepository;

    // --- CRUD Operations ---
    public List<CrewMember> getAllCrew() {
        return crewRepository.findAll();
    }

    public Optional<CrewMember> getCrewById(String id) {
        return crewRepository.findById(id);
    }

    public CrewMember createCrew(CrewMember crew) {
        return crewRepository.save(crew);
    }

    public CrewMember updateCrew(String id, CrewMember updatedCrew) {
        return crewRepository.findById(id).map(crew -> {
            crew.setUsername(updatedCrew.getUsername());
            crew.setAddress(updatedCrew.getAddress());
            crew.setContactNumber(updatedCrew.getContactNumber());
            crew.setActivated(updatedCrew.isActivated());
            return crewRepository.save(crew);
        }).orElseThrow(() -> new RuntimeException("Crew member not found"));
    }

    public void deleteCrew(String id) {
        crewRepository.deleteById(id);
    }

    // --- Assign/Unassign Truck ---
    public CrewMember assignTruck(String crewId, String truckId) {
        CrewMember crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("Crew member not found"));

        Truck truck = truckRepository.findById(truckId)
                .orElseThrow(() -> new RuntimeException("Truck not found"));

        crew.setAssignedTruck(truck);
        return crewRepository.save(crew);
    }

    public CrewMember unassignTruck(String crewId) {
        CrewMember crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("Crew member not found"));

        crew.setAssignedTruck(null);
        return crewRepository.save(crew);
    }

    // --- Query Methods ---
    public List<CrewMember> getAvailableCrew() {
        return crewRepository.findByAvailableTrue();
    }
}
