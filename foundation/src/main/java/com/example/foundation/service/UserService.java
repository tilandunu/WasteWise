package com.example.foundation.service;

import com.example.foundation.dto.request.RegisterUserRequest;
import com.example.foundation.model.Resident;
import com.example.foundation.model.CrewMember;
import com.example.foundation.model.Zone;
import com.example.foundation.repository.UserRepository;
import com.example.foundation.repository.ZoneRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ZoneRepository zoneRepository;

    public UserService(UserRepository userRepository, ZoneRepository zoneRepository) {
        this.userRepository = userRepository;
        this.zoneRepository = zoneRepository;
    }

    // --- Register Resident ---
    public String registerResident(RegisterUserRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        Zone zone = zoneRepository.findById(request.getZoneId())
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        Resident newUser = new Resident(
                request.getUsername(),
                request.getPassword(),
                request.getAddress(),
                request.getContactNumber()
        );

        newUser.setZone(zone);
        newUser.setActivated(false);

        // Set premises type if provided
        if (request.getPremisesType() != null) {
            try {
                newUser.setPremisesType(Resident.PremisesType.valueOf(request.getPremisesType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid premises type");
            }
        }

        userRepository.save(newUser);
        return "User registered successfully. Await activation.";
    }

    // --- Register Crew Member ---
    public CrewMember registerCrewMember(RegisterUserRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        Zone zone = zoneRepository.findById(request.getZoneId())
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        CrewMember newCrew = new CrewMember(
                request.getUsername(),
                request.getPassword(),
                request.getAddress(),
                request.getContactNumber()
        );

        newCrew.setZone(zone);
        newCrew.setAvailable(true);
        newCrew.setActivated(false);
        newCrew.setAssignedTruck(null);

        return userRepository.save(newCrew);
    }

    // --- Login ---
    public Object login(String username, String password) {
        var userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty())
            throw new IllegalArgumentException("User not found");

        var user = userOpt.get();

        if (!user.getPassword().equals(password))
            throw new IllegalArgumentException("Invalid credentials");

        // Uncomment if activation is required:
        // if (!user.isActivated()) throw new IllegalStateException("User not activated");

        return user;
    }

    // --- Get All Residents ---
    public List<Resident> getAllResidents() {
        return userRepository.findAll().stream()
                .filter(user -> user instanceof Resident)
                .map(user -> (Resident) user)
                .toList();
    }
}
