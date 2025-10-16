package com.example.foundation.controller;

import com.example.foundation.dto.request.RegisterUserRequest;
import com.example.foundation.model.Resident;
import com.example.foundation.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // --- Login ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RegisterUserRequest loginRequest) {
        var userOpt = userRepository.findByUsername(loginRequest.getUsername());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("User not found");
        }

        var user = userOpt.get();

        if (!user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        // if (!user.isActivated()) {
        //     return ResponseEntity.status(403).body("User not activated");
        // }

        return ResponseEntity.ok(user);
    }

    // --- Register ---
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserRequest registrationRequest) {
        if (userRepository.findByUsername(registrationRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        Resident newUser = new Resident(
                registrationRequest.getUsername(),
                registrationRequest.getPassword(),
                registrationRequest.getAddress(),
                registrationRequest.getContactNumber()
        );
        newUser.setZone(registrationRequest.getZone());
        newUser.setActivated(false);

        userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully. Await activation.");
    }
}
