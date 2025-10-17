package com.example.foundation.controller;

import com.example.foundation.dto.request.RegisterUserRequest;
import com.example.foundation.model.Resident;
import com.example.foundation.model.CrewMember;
import com.example.foundation.model.Zone;
import com.example.foundation.repository.UserRepository;
import com.example.foundation.repository.ZoneRepository;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final ZoneRepository zoneRepository;

    public UserController(UserRepository userRepository, ZoneRepository zoneRepository) {
        this.userRepository = userRepository;
        this.zoneRepository = zoneRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        // Fetch Zone by ID
        Zone zone = zoneRepository.findById(request.getZoneId())
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        // Create Resident
        Resident newUser = new Resident(
                request.getUsername(),
                request.getPassword(),
                request.getAddress(),
                request.getContactNumber());

        newUser.setZone(zone);
        newUser.setActivated(false);

        // Set Premises Type
        if (request.getPremisesType() != null) {
            try {
                newUser.setPremisesType(Resident.PremisesType.valueOf(request.getPremisesType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid premises type");
            }
        }

        userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully. Await activation.");
    }

    // --- Crew Member Registration ---
    @PostMapping("/register-crew")
    public ResponseEntity<?> registerCrewMember(@RequestBody RegisterUserRequest request) {
        
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        // Fetch Zone by ID
        Zone zone = zoneRepository.findById(request.getZoneId())
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        // Create Crew Member
        CrewMember newCrew = new CrewMember(
                request.getUsername(),
                request.getPassword(),
                request.getAddress(),
                request.getContactNumber()
        );

        newCrew.setZone(zone);
        newCrew.setAvailable(true);
        newCrew.setActivated(false);
        
        // Explicitly set assignedTruck to null (new crew members start unassigned)
        newCrew.setAssignedTruck(null);

        // Save using userRepository instead of crewRepository
        CrewMember savedCrew = userRepository.save(newCrew);
        
        return ResponseEntity.ok(savedCrew );
    }

    // --- Login ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Resident loginRequest) {
        var userOpt = userRepository.findByUsername(loginRequest.getUsername());

        if (userOpt.isEmpty())
            return ResponseEntity.status(401).body("User not found");

        var user = userOpt.get();
        if (!user.getPassword().equals(loginRequest.getPassword()))
            return ResponseEntity.status(401).body("Invalid credentials");

        // if (!user.isActivated()) return ResponseEntity.status(403).body("User not activated");

        return ResponseEntity.ok(user);
    }

    // Get all users of type Resident
    @GetMapping("/all")
    public ResponseEntity<List<Resident>> getAllUsers() {
        List<Resident> residents = userRepository.findAll().stream()
                .filter(user -> user instanceof Resident)
                .map(user -> (Resident) user)
                .toList(); // Java 16+; use Collectors.toList() if older
        return ResponseEntity.ok(residents);
    }

    //get all crew members
    @GetMapping("/crew-members")
    public ResponseEntity<List<CrewMember>> getAllCrewMembers() {
        List<CrewMember> crewMembers = userRepository.findAll().stream()
                .filter(user -> user instanceof CrewMember)
                .map(user -> (CrewMember) user)
                .toList(); // Java 16+; use Collectors.toList() if older
        return ResponseEntity.ok(crewMembers);
    }
}