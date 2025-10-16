package com.example.foundation.controller;

import com.example.foundation.dto.request.RegisterUserRequest;
import com.example.foundation.model.Resident;
import com.example.foundation.model.Zone;
import com.example.foundation.repository.UserRepository;
import com.example.foundation.repository.ZoneRepository;
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

    // --- Register ---
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        // Fetch Zone by ID
        Zone zone = zoneRepository.findById(request.getZoneId())
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        Resident newUser = new Resident(
                request.getUsername(),
                request.getPassword(),
                request.getAddress(),
                request.getContactNumber()
        );

        newUser.setZone(zone);
        newUser.setActivated(false); // initially false

        userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully. Await activation.");
    }

    // --- Login ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Resident loginRequest) {
        var userOpt = userRepository.findByUsername(loginRequest.getUsername());

        if (userOpt.isEmpty()) return ResponseEntity.status(401).body("User not found");

        var user = userOpt.get();
        if (!user.getPassword().equals(loginRequest.getPassword()))
            return ResponseEntity.status(401).body("Invalid credentials");

        // if (!user.isActivated()) return ResponseEntity.status(403).body("User not activated");

        return ResponseEntity.ok(user);
    }
}
