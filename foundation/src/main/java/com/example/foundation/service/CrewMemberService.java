package com.example.foundation.service;

import com.example.foundation.dto.request.RegisterUserRequest;
import com.example.foundation.model.CrewMember;
import com.example.foundation.model.Zone;
import com.example.foundation.repository.UserRepository;
import com.example.foundation.repository.ZoneRepository;
import com.example.foundation.repository.TruckRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrewMemberService {

    private final UserRepository userRepository;
    private final TruckRepository truckRepository;
    private final ZoneRepository zoneRepository;

    public CrewMemberService(UserRepository userRepository, TruckRepository truckRepository , ZoneRepository zoneRepository)  {
        this.userRepository = userRepository;
        this.truckRepository = truckRepository;
        this.zoneRepository = zoneRepository;
    }

    // 🔹 Get all crew members (filtering from user collection)
    public List<CrewMember> getAllCrewMembers() {
        return userRepository.findAll().stream()
                .filter(user -> user instanceof CrewMember)
                .map(user -> (CrewMember) user)
                .toList();
    }

    // 🔹 Get all available crew members
    public List<CrewMember> getAvailableCrewMembers() {
        return userRepository.findAll().stream()
                .filter(user -> user instanceof CrewMember)
                .map(user -> (CrewMember) user)
                .filter(CrewMember::isAvailable)
                .toList();
    }

    // 🔹 Update availability status
    public CrewMember updateAvailability(String crewId, boolean available) {
        var user = userRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("Crew member not found"));

        if (!(user instanceof CrewMember))
            throw new RuntimeException("User is not a crew member");

        CrewMember crew = (CrewMember) user;
        crew.setAvailable(available);
        return userRepository.save(crew);
    }

    // 🔹 Assign a truck to crew member
    public CrewMember assignTruck(String crewId, String truckId) {
        var user = userRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("Crew member not found"));

        if (!(user instanceof CrewMember))
            throw new RuntimeException("User is not a crew member");

        var truck = truckRepository.findById(truckId)
                .orElseThrow(() -> new RuntimeException("Truck not found"));

        CrewMember crew = (CrewMember) user;
        crew.setAssignedTruck(truck);
        crew.setAvailable(false); // once assigned, not available
        return userRepository.save(crew);
    }



    public CrewMember registerCrewMember(RegisterUserRequest request) {

        // Check if username already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Fetch Zone
        Zone zone = zoneRepository.findById(request.getZoneId())
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        // Create new CrewMember
        CrewMember newCrew = new CrewMember(
                request.getUsername(),
                request.getPassword(),
                request.getAddress(),
                request.getContactNumber()
        );

        newCrew.setZone(zone);
        newCrew.setAvailable(true);
        newCrew.setActivated(false);
        newCrew.setAssignedTruck(null); // initially unassigned

        return userRepository.save(newCrew);
    }



    // 🔹 Unassign truck
    public CrewMember unassignTruck(String crewId) {
        var user = userRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("Crew member not found"));

        if (!(user instanceof CrewMember))
            throw new RuntimeException("User is not a crew member");

        CrewMember crew = (CrewMember) user;
        crew.setAssignedTruck(null);
        crew.setAvailable(true);
        return userRepository.save(crew);
    }
}
