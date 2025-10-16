package com.example.foundation.controller;

import com.example.foundation.dto.request.RegisterPremisesRequest;
import com.example.foundation.dto.response.PremisesResponse;
import com.example.foundation.service.RegistrationService;
import com.example.foundation.service.AuthService;
import com.example.foundation.model.User;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/premises")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final AuthService authService;

    public RegistrationController(RegistrationService registrationService, AuthService authService) {
        this.registrationService = registrationService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerPremises(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody RegisterPremisesRequest request) {

        // 1️⃣ Validate Authorization header
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        String idToken = authorizationHeader.substring(7);
        User user;
        try {
            // 2️⃣ Verify Firebase token and get user
            user = authService.verifyIdTokenAndGetUser(idToken);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(401).body("Invalid or expired Firebase token");
        }

        // 3️⃣ Check if user is authorized officer/admin
        // if (user.getRole() != User.Role.OFFICER) {
        //     return ResponseEntity.status(403).body("Unauthorized: Only officers can register premises");
        // }

        // 4️⃣ Delegate to service
        try {
            PremisesResponse response = registrationService.registerPremises(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
